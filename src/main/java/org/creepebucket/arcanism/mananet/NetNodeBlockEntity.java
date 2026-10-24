package org.creepebucket.arcanism.mananet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.creepebucket.arcanism.ModConfig;
import org.creepebucket.arcanism.registries.ModAttachments;
import org.creepebucket.arcanism.registries.ModBlockEntities;
import org.creepebucket.arcanism.registries.ModBlocks;
import org.creepebucket.arcanism.utils.Mana;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NetNodeBlockEntity extends BlockEntity {
    public Mana loadCopy = new Mana(), cacheCopy = new Mana();

    public NetNodeBlockEntity(BlockPos pos, BlockState blockState) {
        this(resolve_type(blockState), pos, blockState);
    }

    public NetNodeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static BlockEntityType<?> resolve_type(BlockState blockState) {
        if (blockState.is(ModBlocks.HORIZONTAL_MANA_CONNECTOR.get())) return ModBlockEntities.HORIZONTAL_MANA_CONNECTOR_BLOCK_ENTITY.get();
        if (blockState.is(ModBlocks.VERTICAL_MANA_CONNECTOR.get())) return ModBlockEntities.VERTICAL_MANA_CONNECTOR_BLOCK_ENTITY.get();
        return ModBlockEntities.BASIC_MANA_CONNECTOR_BLOCK_ENTITY.get();
    }

    public void setLoadW(Mana load) {
        loadCopy = loadCopy.add(load.scale(1.0 / 20.0));
        getNetworkData().addLoadW(load);
    }

    public void setCache(Mana cache) {
        cacheCopy = cacheCopy.add(cache);
        getNetworkData().addCache(cache);
    }

    public void connect(Level level, BlockPos connectedPos, Direction connectedFace, Direction selfFace) {
        if (connectedPos.equals(getData(ModAttachments.CONNECTIONS).get(selfFace))) return;
        if (!(level.getBlockEntity(connectedPos) instanceof NetNodeBlockEntity connected)) return;

        // 断开两侧该面上已有的连接, 避免旧对端残留
        if (getData(ModAttachments.CONNECTIONS).containsKey(selfFace)) disconnect(selfFace);
        if (connected.getData(ModAttachments.CONNECTIONS).containsKey(connectedFace)) connected.disconnect(connectedFace);

        var selfId = getData(ModAttachments.NETWORK_ID);
        var connectedId = connected.getData(ModAttachments.NETWORK_ID);
        var selfData = NetworkManaManager.getManaData(level, selfId);
        var connectedData = NetworkManaManager.getManaData(level, connectedId);
        var selfCurrent = selfData.getCurrent();
        var connectedCurrent = connectedData.getCurrent();

        var connections = new HashMap<>(connected.getData(ModAttachments.CONNECTIONS));
        connections.put(connectedFace, getBlockPos());
        connected.setData(ModAttachments.CONNECTIONS, connections);

        var selfConnections = new HashMap<>(getData(ModAttachments.CONNECTIONS));
        selfConnections.put(selfFace, connectedPos);
        setData(ModAttachments.CONNECTIONS, selfConnections);

        rebuildNetworkId(level, getBlockPos());

        var newId = getData(ModAttachments.NETWORK_ID);
        var newData = NetworkManaManager.getManaData(level, newId);
        newData.setCurrent(selfId.longValue() == connectedId.longValue() ? selfCurrent : selfCurrent.add(connectedCurrent));
        if (selfId.longValue() != connectedId.longValue()) {
            newData.setLoad(selfData.getStoredLoad().add(connectedData.getStoredLoad()));
            newData.setCache(selfData.getStoredCache().add(connectedData.getStoredCache()));
        }
        NetworkManaManager.touch(level, newId);
    }

    public void disconnect(BlockPos connectedPos) {
        var connections = new HashMap<>(getData(ModAttachments.CONNECTIONS));
        if (!connections.entrySet().removeIf(e -> e.getValue().equals(connectedPos))) return;
        setData(ModAttachments.CONNECTIONS, connections);

        // 若对方不是方块实体, 跳过断开对方连接的步骤
        if (!getLevel().getBlockState(connectedPos).hasBlockEntity()
                || !getLevel().getBlockEntity(connectedPos).hasData(ModAttachments.CONNECTIONS)) return;

        var level = getLevel();
        var connected = level.getBlockEntity(connectedPos);
        var connectedConnections = new HashMap<>(connected.getData(ModAttachments.CONNECTIONS));
        connectedConnections.entrySet().removeIf(e -> e.getValue().equals(getBlockPos()));
        connected.setData(ModAttachments.CONNECTIONS, connectedConnections);

        var oldCurrent = NetworkManaManager.getManaData(level, getData(ModAttachments.NETWORK_ID)).getCurrent();
        var selfNodes = collectNetwork(level, getBlockPos());
        var otherNodes = collectNetwork(level, connectedPos);
        var selfId = computeNetworkId(level, selfNodes);
        var otherId = computeNetworkId(level, otherNodes);
        applyNetworkId(selfNodes, selfId);
        applyNetworkId(otherNodes, otherId);

        // 未发生分割时魔力仍属于同一个网络
        if (selfId == otherId) return;

        var selfLoad = new Mana();
        var selfCache = new Mana();
        for (var node : selfNodes) {
            selfLoad = selfLoad.add(node.loadCopy);
            selfCache = selfCache.add(node.cacheCopy);
        }
        var otherLoad = new Mana();
        var otherCache = new Mana();
        for (var node : otherNodes) {
            otherLoad = otherLoad.add(node.loadCopy);
            otherCache = otherCache.add(node.cacheCopy);
        }

        var totalCache = selfCache.add(otherCache);
        var selfMana = switch (ModConfig.CONFIG.manaNetSplitMode.get()) {
            case HALF -> oldCurrent.scale(0.5);
            case NODE_RATIO -> oldCurrent.scale((double) selfNodes.size() / (selfNodes.size() + otherNodes.size()));
            case LARGEST -> selfNodes.size() >= otherNodes.size() ? oldCurrent : new Mana();
            case RATIO -> new Mana(
                    totalCache.getRadiation() == 0 ? oldCurrent.getRadiation() * 0.5 : oldCurrent.getRadiation() * selfCache.getRadiation() / totalCache.getRadiation(),
                    totalCache.getTemperature() == 0 ? oldCurrent.getTemperature() * 0.5 : oldCurrent.getTemperature() * selfCache.getTemperature() / totalCache.getTemperature(),
                    totalCache.getMomentum() == 0 ? oldCurrent.getMomentum() * 0.5 : oldCurrent.getMomentum() * selfCache.getMomentum() / totalCache.getMomentum(),
                    totalCache.getPressure() == 0 ? oldCurrent.getPressure() * 0.5 : oldCurrent.getPressure() * selfCache.getPressure() / totalCache.getPressure());
        };

        var selfData = NetworkManaManager.getManaData(level, selfId);
        var otherData = NetworkManaManager.getManaData(level, otherId);
        selfData.setCurrent(selfMana);
        selfData.setLoad(selfLoad);
        selfData.setCache(selfCache);
        otherData.setCurrent(oldCurrent.subtract(selfMana));
        otherData.setLoad(otherLoad);
        otherData.setCache(otherCache);
        NetworkManaManager.touch(level, selfId);
        NetworkManaManager.touch(level, otherId);
    }

    public void disconnect(Direction selfFace) {
        var connectedPos = getData(ModAttachments.CONNECTIONS).get(selfFace);
        if (connectedPos != null) disconnect(connectedPos);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (!hasData(ModAttachments.CONNECTIONS)) setData(ModAttachments.CONNECTIONS, new HashMap<>());
        if (!getLevel().isClientSide() && getData(ModAttachments.NETWORK_ID) == 0L) setData(ModAttachments.NETWORK_ID, getBlockPos().asLong());
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        // 移除对向连接
        for (var connectedPos : new HashMap<>(getData(ModAttachments.CONNECTIONS)).values()) {
            disconnect(connectedPos);
        }
    }

    public static void rebuildNetworkId(Level level, BlockPos startPos) {
        if (!(level.getBlockEntity(startPos) instanceof NetNodeBlockEntity)) return;
        var nodes = collectNetwork(level, startPos);
        applyNetworkId(nodes, computeNetworkId(level, nodes));
    }

    public static List<NetNodeBlockEntity> collectNetwork(Level level, BlockPos startPos) {
        var queue = new ArrayDeque<BlockPos>();
        var visited = new HashSet<BlockPos>();
        var nodes = new ArrayList<NetNodeBlockEntity>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            var pos = queue.removeFirst();

            var blockEntity = (NetNodeBlockEntity) level.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);
            nodes.add(blockEntity);

            for (var nextPos : blockEntity.getData(ModAttachments.CONNECTIONS).values()) {
                if (!visited.add(nextPos)) continue;
                queue.addLast(nextPos);
            }
        }

        return nodes;
    }

    public static long computeNetworkId(Level level, List<NetNodeBlockEntity> nodes) {
        var dimensionId = (long) level.dimension().identifier().hashCode() << 32;
        var networkId = Long.MAX_VALUE;
        for (var node : nodes) networkId = Math.min(networkId, dimensionId ^ node.getBlockPos().asLong());
        return networkId;
    }

    public static void applyNetworkId(List<NetNodeBlockEntity> nodes, long networkId) {
        for (var node : nodes) node.setData(ModAttachments.NETWORK_ID, networkId);
    }

    public NetworkManaData getNetworkData() {
        return NetworkManaManager.getManaData(getLevel(), getData(ModAttachments.NETWORK_ID));
    }
}
