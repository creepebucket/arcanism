package org.creepebucket.arcanism.mananet.machines.buffer;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.creepebucket.arcanism.mananet.NetNodeBlockEntity;
import org.creepebucket.arcanism.mananet.machines.MachineBlockEntity;
import org.creepebucket.arcanism.registries.ModDataComponents;
import org.creepebucket.arcanism.utils.Mana;
import org.creepebucket.arcanism.utils.StoredMana;

public class ManaBufferBlockEntity extends MachineBlockEntity implements GeoBlockEntity {
	public AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public double baseStorage, baseExpansion, baseExpansionPower, maxChargePower;
	public int chargeSlotCount;
	public double powerFact = 0d;
	public double chargeRate = 1d;
	public int connectHeight;
	public SimpleContainer chargeContainer = new SimpleContainer(5) {
		@Override
		public void setChanged() {
			ManaBufferBlockEntity.this.setChanged();
		}
	};

	public ManaBufferBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		powerFact = input.getDoubleOr("power_fact", 0d);
		chargeRate = input.getDoubleOr("charge_rate", 1d);

		int chargeIndex = 0;
		for (var stack : input.listOrEmpty("charge_items", ItemStack.OPTIONAL_CODEC)) {
			chargeContainer.setItem(chargeIndex++, stack);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putDouble("power_fact", powerFact);
		output.putDouble("charge_rate", chargeRate);

		var chargeItems = output.list("charge_items", ItemStack.OPTIONAL_CODEC);
		for (var stack : chargeContainer.getItems()) chargeItems.add(stack);
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

		// 充能逻辑
		var budget = entity.chargeRate * entity.maxChargePower / 20d;
		for (int i = 0; i < entity.chargeSlotCount; i++) {
			var stack = entity.chargeContainer.getItem(i);
			var stored = stack.get(ModDataComponents.MANA.get());
			if (stored == null) continue;

			// r
			var planToChargeR = Math.min(stored.capacity.getRadiation() - stored.current.getRadiation(), budget);
			if (network.canProduce(new Mana(planToChargeR, 0d, 0d, 0d))) {
				stored = new StoredMana(stored.current.add(new Mana(planToChargeR, 0d, 0d, 0d)), stored.capacity);
				network.setLoadW(new Mana(planToChargeR * 20, 0d, 0d, 0d));
				budget -= planToChargeR;
			}

			// t
			var planToChargeT = Math.min(stored.capacity.getTemperature() - stored.current.getTemperature(), budget);
			if (network.canProduce(new Mana(0d, planToChargeT, 0d, 0d))) {
				stored = new StoredMana(stored.current.add(new Mana(0d, planToChargeT, 0d, 0d)), stored.capacity);
				network.setLoadW(new Mana(0d, planToChargeT * 20, 0d, 0d));
				budget -= planToChargeT;
			}

			// m
			var planToChargeM = Math.min(stored.capacity.getMomentum() - stored.current.getMomentum(), budget);
			if (network.canProduce(new Mana(0d, 0d, planToChargeM, 0d))) {
				stored = new StoredMana(stored.current.add(new Mana(0d, 0d, planToChargeM, 0d)), stored.capacity);
				network.setLoadW(new Mana(0d, 0d, planToChargeM * 20, 0d));
				budget -= planToChargeM;
			}

			// p
			var planToChargeP = Math.min(stored.capacity.getPressure() - stored.current.getPressure(), budget);
			if (network.canProduce(new Mana(0d, 0d, 0d, planToChargeP))) {
				stored = new StoredMana(stored.current.add(new Mana(0d, 0d, 0d, planToChargeP)), stored.capacity);
				network.setLoadW(new Mana(0d, 0d, 0d, planToChargeP * 20));
				budget -= planToChargeP;
			}

			stack.set(ModDataComponents.MANA.get(), stored);
		}
		entity.chargeContainer.setChanged();
	}
}
