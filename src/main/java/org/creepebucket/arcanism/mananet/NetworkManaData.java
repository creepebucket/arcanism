package org.creepebucket.arcanism.mananet;

import net.minecraft.world.level.Level;
import org.creepebucket.arcanism.utils.Mana;

import java.util.Map;

public class NetworkManaData {
    public Long id;
    public Level level;
    public Map<String, Mana> data;

    public NetworkManaData(Long id, Level level, Map<String, Mana> data) {
        this.data = data;
        this.id = id;
        this.level = level;
    }

    public Mana getCurrent(){
        return data.get("current");
    }

    public NetworkManaData setCurrent(Mana current){
        data.put("current", current);
        return this;
    }

    public Mana getLoad(){
        return NetworkManaManager.getCached(level, id, "load", data.get("load"));
    }

    /**
     * 获取本刻已存储的负载, 不走上一刻的缓存
     */
    public Mana getStoredLoad(){
        return data.get("load");
    }

    public NetworkManaData setLoad(Mana load){
        data.put("load", load);
        return this;
    }

    public NetworkManaData addLoadW(Mana load){
        NetworkManaManager.touch(level, id);
        data.put("load", data.get("load").add(load.scale(1.0 / 20.0)));
        return this;
    }

    public Mana getCache(){
        return NetworkManaManager.getCached(level, id, "cache", data.get("cache"));
    }

    /**
     * 获取本刻已存储的容量, 不走上一刻的缓存
     */
    public Mana getStoredCache(){
        return data.get("cache");
    }

    public NetworkManaData setCache(Mana cache){
        data.put("cache", cache);
        return this;
    }

    public NetworkManaData addCache(Mana cache){
        NetworkManaManager.touch(level, id);
        data.put("cache", data.get("cache").add(cache));
        return this;
    }

    /**
     * 获取下一刻的魔力储量
     */
    public Mana getNext(){
        return getCurrent().subtract(getLoad());
    }

    /**
     * 获取下一刻能不能继续运行
     */
    public boolean canProduce(Mana load){
        return !new Mana().anyGreaterThan(getNext().subtract(load.scale(1.0 / 20.0)));
    }
}
