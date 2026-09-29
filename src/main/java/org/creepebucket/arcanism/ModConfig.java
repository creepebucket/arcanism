package org.creepebucket.arcanism;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
	public static final ModConfig CONFIG;
	public static final ModConfigSpec CONFIG_SPEC;

	public final ModConfigSpec.DoubleValue fuelValueMultiplier;
	public final ModConfigSpec.BooleanValue moreBalancedFuel;
	public final ModConfigSpec.BooleanValue disableAnimations;
	public final ModConfigSpec.DoubleValue castMass;
	public final ModConfigSpec.DoubleValue breakEnergyPerHardness;
	public final ModConfigSpec.DoubleValue globalCastCostMult;

	static {
		Pair<ModConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
				.configure(ModConfig::new);
		CONFIG = pair.getLeft();
		CONFIG_SPEC = pair.getRight();
	}

	public ModConfig(ModConfigSpec.Builder builder) {
		/*
		builder.push("examples");

		exampleString = builder
				.comment("A string value example")
				.translation("arcanism.config.example_string")
				.define("example_string", "default_value");

		exampleBoolean = builder
				.comment("A boolean value example")
				.translation("arcanism.config.example_boolean")
				.define("example_boolean", true);

		exampleInt = builder
				.comment("An integer value example, range 0-1000")
				.translation("arcanism.config.example_int")
				.defineInRange("example_int", 100, 0, 1000);

		exampleLong = builder
				.comment("A long value example, range 0-100000")
				.translation("arcanism.config.example_long")
				.defineInRange("example_long", 5000L, 0L, 100000L);

		exampleDouble = builder
				.comment("A double value example, range 0.0-10.0")
				.translation("arcanism.config.example_double")
				.defineInRange("example_double", 1.0, 0.0, 10.0);

		exampleList = builder
				.comment("A list value example")
				.translation("arcanism.config.example_list")
				.defineList("example_list", List.of("a", "b", "c"), () -> "new_entry", (obj) -> true);

		exampleEnum = builder
				.comment("An enum value example")
				.translation("arcanism.config.example_enum")
				.defineEnum("example_enum", ExampleEnum.OPTION_A);

		exampleWhitelist = builder
				.comment("A whitelisted string value example")
				.translation("arcanism.config.example_whitelist")
				.defineInList("example_whitelist", "option_1", Arrays.asList("option_1", "option_2", "option_3"));

		builder.pop(); */

		builder.push("gameplay");

		fuelValueMultiplier = builder.comment("每刻燃烧时间对应的燃料燃值")
				.translation("arcanism.config.fuel_value_multplier")
				.defineInRange("fuel_value_multplier", 8e6, 0.0, Double.MAX_VALUE);

		moreBalancedFuel = builder.comment("使燃料热值更贴近现实")
				.translation("arcanism.config.more_balanced_fuel")
				.define("more_balanced_fuel", false);

		castMass = builder.comment("法术消耗计算使用的固定质量(kg)")
				.translation("arcanism.config.cast_mass")
				.defineInRange("cast_mass", 70.0, 0.0, Double.MAX_VALUE);

		breakEnergyPerHardness = builder.comment("每点方块硬度对应的破坏魔力消耗(J)")
				.translation("arcanism.config.break_energy_per_hardness")
				.defineInRange("break_energy_per_hardness", 1e6, 0.0, Double.MAX_VALUE);

		globalCastCostMult = builder.comment("所有法术魔力消耗的全局乘数")
				.translation("arcanism.config.global_cast_cost_mult")
				.defineInRange("global_cast_cost_mult", 1.0, 0.0, Double.MAX_VALUE);

		builder.pop();

		builder.push("visual");

		disableAnimations = builder.comment("禁用动画")
				.translation("arcanism.config.disable_animations")
				.define("disable_animations", false);

		builder.pop();
	}
}
