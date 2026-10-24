package org.creepebucket.arcanism.mananet.machines.generator.solar_panel;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.creepebucket.arcanism.mananet.NetNodeBlockEntity;
import org.creepebucket.arcanism.mananet.machines.MachineBlockEntity;
import org.creepebucket.arcanism.registries.ModBlockEntities;
import org.creepebucket.arcanism.utils.Mana;

import static org.creepebucket.arcanism.utils.ModUtils.getTempKelvin;

public class SolarPanelBlockEntity extends MachineBlockEntity implements GeoBlockEntity {
    public double solarConstant = 1367, directIrradiance, diffuseIrradiance, atmosphericTransmittanceDiffuse;
    public double atmosphericTransmittanceDirect = 0.8, airMass, weatherFactDirect, weatherFactDiffuse, panelArea = 6;
    public double baseTemperature, cellTemperature, thermalFact, materialFact = 0.15, altitudeFact, power, efficiencyFact;

    public final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOLAR_PANEL_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level.isClientSide()) {
            return;
        }

        // 重新计算发电数据
        baseTemperature = getTempKelvin(getBlockPos(), (ServerLevel) level);
        altitudeFact = Math.exp((double) getBlockPos().getY() / 8400);
        //
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        if (level.isClientSide()) return;

        // ========== 发电计算逻辑 ========== //

        //             太阳辐照度             //

        if (level.isThundering()) {
            this.weatherFactDiffuse = 0.15;
            this.weatherFactDirect = 0.05;
        }
        else if (level.isRaining()) {
            this.weatherFactDiffuse = 1.2;
            this.weatherFactDirect = 0.5;
        }
        else {
            this.weatherFactDiffuse = 1;
            this.weatherFactDirect = 1;
        }

        var time = level.getOverworldClockTime() % 24000;
        var zenith = Math.abs(time - 6000) * Math.PI / 12000.0F;
        var alpha = Math.PI / 2.0 - zenith;
        var alphaDeg = Math.toDegrees(alpha);

        // Kasten-Young 公式
        this.airMass = time < 12000 ? 1.0 / (Math.sin(alpha) + 0.50572 * Math.pow(alphaDeg + 6.07995, -1.6364)) : Double.MAX_VALUE;
        var directIrradianceFact = Math.pow(this.atmosphericTransmittanceDirect, this.airMass * this.altitudeFact);
        this.directIrradiance = this.solarConstant * directIrradianceFact * this.weatherFactDirect;

        // Hottel 散射关系
        this.atmosphericTransmittanceDiffuse = Math.max(0.2710 - 0.2939 * directIrradianceFact, 0) * smoothstep(0, 6, Math.toDegrees(alpha));
        this.diffuseIrradiance = 50 * this.atmosphericTransmittanceDiffuse * this.weatherFactDiffuse;

        //              效率系数              //

        this.cellTemperature = this.baseTemperature + 30 * Math.sin((Math.PI * 2 / 24000) * time) + 15;
        this.thermalFact = 1 - .005 * (this.cellTemperature - 25 - 273.15);
        this.efficiencyFact = this.thermalFact * this.materialFact * 100;

        // ========== 网络连接逻辑 ========== //

        if (!level.isClientSide() && level.getBlockEntity(pos.east(1)) instanceof NetNodeBlockEntity) {
            connect(level, pos.east(1), Direction.WEST, Direction.EAST);
        }
        if (!level.isClientSide() && level.getBlockEntity(pos.west(1)) instanceof NetNodeBlockEntity) {
            connect(level, pos.west(1), Direction.EAST, Direction.WEST);
        }
        if (!level.isClientSide() && level.getBlockEntity(pos.south(1)) instanceof NetNodeBlockEntity) {
            connect(level, pos.south(1), Direction.NORTH, Direction.SOUTH);
        }
        if (!level.isClientSide() && level.getBlockEntity(pos.north(1)) instanceof NetNodeBlockEntity) {
            connect(level, pos.north(1), Direction.SOUTH, Direction.NORTH);
        }

        this.power = this.enabled ? (this.directIrradiance + this.diffuseIrradiance) * this.panelArea * this.efficiencyFact * 0.01 : 0;
        var load = new Mana(-this.power, 0d, 0d, 0d);

        if (getNetworkData().canProduce(load)) setLoadW(load);
        setCache(new Mana(2000d, 2000d, 2000d, 2000d));
    }

    private static double smoothstep(double edge0, double edge1, double x) {
        if (x <= edge0) return 0.0d;
        if (x >= edge1) return 1.0d;
        var t = (x - edge0) / (edge1 - edge0);
        return t * t * (3.0d - 2.0d * t);
    }
}
