package org.creepebucket.arcanism.gui.help.contents.config;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class NetworkWidget extends HelpWindowWidgets.HelpBodyWidget {
    public NetworkWidget() {
        super(Component.literal("网络"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("网络相关的配置都位于 config/arcanism-common.toml 的 [network] 段中, 修改后需要重启游戏或服务器才会生效."));
        addHeading("import", Component.literal("客户端插值"));
        addParagraph(Component.literal("服务端会按固定间隔把机器魔力/工作进度等数据同步到客户端. 开启插值后, 客户端会把相邻两次同步之间的数值平滑过渡, 显示更顺滑, 但代价是显示值会比真实值晚一段时间."));
        addEntry("1_small", Component.literal("client_side_interpolation"), Component.literal("启用服务端数据的客户端插值, 默认关闭. 该选项不会改变服务端的数据回传时间."));
        addEntry("2_small", Component.literal("client_interpolation_window"), Component.literal("客户端插值时间窗口(刻), 默认 15, 最小为 1. 窗口越长过渡越平滑, 延迟也越明显."));
        addHeading("delay", Component.literal("同步间隔"));
        addEntry("1_small", Component.literal("server_data_sync_interval"), Component.literal("服务端数据回传间隔(刻), 默认 15, 最小为 1. 调小可以让客户端数据更及时, 但会增加网络流量."));
        addImportant(Component.literal("插值窗口与同步间隔是两回事: 前者只影响客户端的显示平滑程度, 后者才真正改变数据从服务端发出的频率."));
    }
}
