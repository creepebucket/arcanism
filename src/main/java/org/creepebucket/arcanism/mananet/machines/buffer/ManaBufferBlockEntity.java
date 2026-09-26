package org.creepebucket.arcanism.mananet.machines.buffer;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.creepebucket.arcanism.mananet.NetNodeBlockEntity;
import org.creepebucket.arcanism.mananet.machines.MachineBlockEntity;
import org.creepebucket.arcanism.utils.Mana;

public class ManaBufferBlockEntity extends MachineBlockEntity implements GeoBlockEntity {
	public AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public double baseStorage, baseExpansion, baseExpansionPower, maxChargePower;
	public int chargeSlotCount;
	public double powerFact = 0d;
	public double chargeRate = 1d;
	public int connectHeight;

	public ManaBufferBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		powerFact = input.getDoubleOr("power_fact", 0d);
		chargeRate = input.getDoubleOr("charge_rate", 1d);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putDouble("power_fact", powerFact);
		output.putDouble("charge_rate", chargeRate);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, ManaBufferBlockEntity entity) {
		if (level.isClientSide()) return;

		var network = entity.getNetworkData();

		// 扩容逻辑
		var neededPower = new Mana((Math.pow(11, entity.powerFact) - 1) * entity.baseExpansionPower, 0d, 0d, 0d);
		if (network.canProduce(neededPower)) {
			network.setLoadW(neededPower);
			var expanded = entity.powerFact * entity.baseExpansion;
			network.setCache(new Mana(expanded, expanded, expanded, expanded));
		}
		network.setCache(new Mana(entity.baseStorage, entity.baseStorage, entity.baseStorage, entity.baseStorage));

		if (level.getBlockEntity(pos.above(entity.connectHeight)) instanceof NetNodeBlockEntity nodeBe) {
			nodeBe.connect(level, pos, Direction.UP, Direction.DOWN);
		}
	}
}
