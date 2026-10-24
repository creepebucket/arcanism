package org.creepebucket.arcanism.gui.lib.api;

import org.creepebucket.arcanism.ModConfig;

import java.util.*;
import java.util.function.BiConsumer;

import static org.creepebucket.arcanism.utils.ModUtils.now;

public class DataManager {

    // 内部协议常量
    private static final String KEY_PULL_REQUEST = "$internal:pull";

    // 核心存储
    final Map<String, Object> values = new HashMap<>();
    final Map<String, TreeMap<Double, Double>> interpolations = new HashMap<>(); // <key, <time, value>>
    final Map<String, SyncMode> modes = new HashMap<>();
	public final Map<String, Runnable> onFirstArrival = new HashMap<>();
	final Map<String, List<Runnable>> onChange = new HashMap<>();

    // 待拉取列表 (客户端用)
    private final Set<String> pendingPulls = new LinkedHashSet<>();

    // 网络回调
    private BiConsumer<String, Object> sendToServer;
    private BiConsumer<String, Object> sendToClient;
    public boolean clientSide;

    // 绑定网络发送器
    public void bindServerSender(BiConsumer<String, Object> sender) {
        this.sendToServer = sender;
    }

    public void bindClientSender(BiConsumer<String, Object> sender) {
        this.sendToClient = sender;
    }

    public Object get(String key) {

        var value = values.get(key);
        if (!clientSide || !ModConfig.CONFIG.clientSideInterpolation.get() || !(value instanceof Double)) return value;

        // 算插值
        var history = interpolations.get(key);
        if (history == null) return value;
        double currentTime = now();
        var kList = new ArrayList<>(history.keySet());
        var vList = new ArrayList<>(history.values());
        kList.add(currentTime); // 积累最后一段未知数据时间
        var windowLength = (double) ModConfig.CONFIG.clientInterpolationWindow.get() / 20;

        double accumulated = 0;
        for (int i = 0; i < vList.size(); i++) {
            accumulated += vList.get(i) * Math.max(0, kList.get(i+1) - Math.max(currentTime - windowLength, kList.get(i))) / windowLength;
        }
        return accumulated;
    }

    /**
     * 1. 注册数据
     * 如果是 S2C 且当前没有发往客户端的能力(说明是客户端)，自动加入拉取列表。
     */
    public <T> DynamicValue<T> register(String key, SyncMode mode, T initialValue) {
        if (!values.containsKey(key)) {

            values.put(key, initialValue);
            modes.put(key, mode);
            if (clientSide && ModConfig.CONFIG.clientSideInterpolation.get() && initialValue instanceof Double v)
                interpolations.computeIfAbsent(key, k -> new TreeMap<>()).put(now(), v);
        }

        // 客户端侧逻辑：如果是服务端控制的数据，启动时需要去拉取
        if ((mode == SyncMode.S2C || mode == SyncMode.BOTH) && sendToClient == null) {
            pendingPulls.add(key);
        }
        return new DynamicValue<>(this, key);
    }

    /**
     * 2. 发送拉取请求 (客户端调用)
     * 把所有积压的 key 打包发给服务端
     */
    public void flushPullRequests() {
        if (!pendingPulls.isEmpty() && sendToServer != null) {
            sendToServer.accept(KEY_PULL_REQUEST, String.join("\n", pendingPulls));
            pendingPulls.clear();
        }
    }

    /**
     * 3. 核心写逻辑 (由 DynamicValue 调用)
     * 更新本地 -> 判断模式 -> 发送网络包
     */
	public void update(String key, Object newVal) {
		apply(key, newVal);

        // 尝试发送网络包
        SyncMode mode = modes.get(key);

        // 如果我是客户端 (有发往服务端的通道) 且模式允许 C2S
        if (sendToServer != null && (mode == SyncMode.C2S || mode == SyncMode.BOTH)) {
            sendToServer.accept(key, newVal);
        }

        // 如果我是服务端 (有发往客户端的通道) 且模式允许 S2C
        if (sendToClient != null && (mode == SyncMode.S2C || mode == SyncMode.BOTH)) {
            sendToClient.accept(key, newVal);
        }
    }

    /**
     * 本地写入, 不发网络包
     */
	public void apply(String key, Object newVal) {
		Object oldVal = values.put(key, newVal);
		fire(key);
		if (!Objects.equals(oldVal, newVal)) fireChange(key);

        // Double插值的特殊处理
        if (clientSide && ModConfig.CONFIG.clientSideInterpolation.get() && newVal instanceof Double value) {
            interpolations.computeIfAbsent(key, k -> new TreeMap<>()).put(now(), value);

            // 清理过时数据
            // 神秘ai代码保留一个过期点, 插值真TM难写
            TreeMap<Double, Double> map = interpolations.get(key);
            double cutoff = now() - (double) ModConfig.CONFIG.clientInterpolationWindow.get() / 20;
            Double keep = map.lowerKey(cutoff);
            if (keep != null) map.headMap(keep, false).clear();
        }
    }

    /**
     * 4. 核心读逻辑 (网络包入口)
     * 处理拉取请求 OR 处理数据更新
     */
    public boolean handlePacket(String key, Object val) {
        // === 情况 A: 处理拉取请求 (服务端逻辑) ===
        if (KEY_PULL_REQUEST.equals(key)) {
            if (sendToClient == null) return true; // 没法回话就忽略
            // 解析 key 列表，直接把当前值发回去
            for (String reqKey : ((String) val).split("\n")) {
                if (values.containsKey(reqKey)) {
                    sendToClient.accept(reqKey, values.get(reqKey));
                }
            }
            return true;
        }

        // === 情况 B: 普通数据同步 ===
        if (!values.containsKey(key)) return false; // 未知 key，不处理
        apply(key, val);

        // 特殊处理：如果是 C2S (客户端改的)，服务端收到后通常要广播回给客户端确认
        SyncMode mode = modes.get(key);
        if (sendToClient != null && (mode == SyncMode.C2S || mode == SyncMode.BOTH)) {
            sendToClient.accept(key, val);
        }

        return true;
    }

	private void fire(String key) {
		Runnable hook = onFirstArrival.remove(key);
		if (hook != null) hook.run();
	}

	private void fireChange(String key) {
		List<Runnable> changeHooks = onChange.get(key);
		if (changeHooks != null) for (Runnable h : changeHooks) h.run();
	}
}
