package org.creepebucket.arcanism.gui.machines.buffer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.api.DynamicValue;
import org.creepebucket.arcanism.gui.lib.api.ThemeTemplate;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.gui.lib.widgets.*;
import org.creepebucket.arcanism.gui.machines.api.MachineScreen;
import org.creepebucket.arcanism.gui.machines.api.MachineWidgets;
import org.creepebucket.arcanism.utils.ModColors;

import java.util.function.Supplier;

import static org.creepebucket.arcanism.gui.lib.api.Coordinate.*;

public class ManaBufferScreen extends MachineScreen<ManaBufferMenu> {

	public ManaBufferScreen(ManaBufferMenu menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}

	@Override
	public void buildWidget() {
		super.buildWidget();

		var energyInfoWindow = new MachineWidgets.InformationWindowWidget(fromCenter(-200, -105), fromTopLeft(250, 100), Component.translatable("gui.arcanism.machine.buffer.window.mana_info"), 191, 100);
		addWidget(energyInfoWindow);


		var energyInfoGraphDiv = new Widget.BlankWidget(fromTopLeft(0, 17), fromTopRight(0, 53));
		energyInfoWindow.addChild(energyInfoGraphDiv);

		// 需要等待数据回传
		Runnable doWhenAllDataArrives = () -> {

			var expansionRatio = menu.baseStorage.get() / (menu.baseExpansion.get() + menu.baseStorage.get());
			Supplier<Double> actualExpansion = () -> menu.baseStorage.get() / (menu.powerFact.get() * menu.baseExpansion.get() + menu.baseStorage.get());

			// r
			energyInfoGraphDiv.addChild(new ProgressBarWidget(custom(expansionRatio, 54 - 108 * expansionRatio, 0, 8), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(1.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoGraphDiv.addChild(new ProgressBarWidget(fromTopLeft(54, 8), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.radiationStorageJ,
					DynamicValue.fromSupplier(() -> menu.radiationCacheJ.get() * actualExpansion.get()))).mainColor(ModColors.MAIN_COLOR_R).bgColor(0x1fffffff);

			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopLeft(7, 8), menu.radiationStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_R));
			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopRight(-8, 8), menu.radiationCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_R).rightAlign());

			// t
			energyInfoGraphDiv.addChild(new ProgressBarWidget(custom(expansionRatio, 54 - 108 * expansionRatio, 0, 20), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(1.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoGraphDiv.addChild(new ProgressBarWidget(fromTopLeft(54, 20), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.temperatureStorageJ,
					DynamicValue.fromSupplier(() -> menu.temperatureCacheJ.get() * actualExpansion.get()))).mainColor(ModColors.MAIN_COLOR_T).bgColor(0x1fffffff);

			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopLeft(7, 20), menu.temperatureStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_T));
			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopRight(-8, 20), menu.temperatureCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_T).rightAlign());

			// m
			energyInfoGraphDiv.addChild(new ProgressBarWidget(custom(expansionRatio, 54 - 108 * expansionRatio, 0, 32), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(1.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoGraphDiv.addChild(new ProgressBarWidget(fromTopLeft(54, 32), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.momentumStorageJ,
					DynamicValue.fromSupplier(() -> menu.momentumCacheJ.get() * actualExpansion.get()))).mainColor(ModColors.MAIN_COLOR_M).bgColor(0x1fffffff);

			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopLeft(7, 32), menu.momentumStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_M));
			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopRight(-8, 32), menu.momentumCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_M).rightAlign());

			// p
			energyInfoGraphDiv.addChild(new ProgressBarWidget(custom(expansionRatio, 54 - 108 * expansionRatio, 0, 44), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(1.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoGraphDiv.addChild(new ProgressBarWidget(fromTopLeft(54, 44), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.pressureStorageJ,
					DynamicValue.fromSupplier(() -> menu.pressureCacheJ.get() * actualExpansion.get()))).mainColor(ModColors.MAIN_COLOR_P).bgColor(0x1fffffff);

			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopLeft(7, 44), menu.pressureStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_P));
			energyInfoGraphDiv.addChild(new NumberDisplayWidget(fromTopRight(-8, 44), menu.pressureCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_P).rightAlign());

		};

		int[] pending = {2};  // 何意味lambda
		Runnable onDataArrive = () -> {
			if (--pending[0] == 0) doWhenAllDataArrives.run();
		};
		menu.baseStorage.whenFirstDataArrivesDo(onDataArrive);
		menu.baseExpansion.whenFirstDataArrivesDo(onDataArrive);

		// 图例
		energyInfoWindow.addChild(new RectangleWidget(fromBottomLeft(7, -7), fromTopLeft(7, 7)).mainColor(-1).bottomAlignY());
		var text1 = energyInfoWindow.addChild(new TextWidget(fromBottomLeft(16, -5), Component.translatable("gui.arcanism.machine.buffer.label.stored_mana")).noShadow().bottomAlignY());
		text1.addChild(new RectangleWidget(fromTopRight(2, 0), fromTopLeft(7, 7)).mainColor(0x1fffffff));
		var text2 = text1.addChild(new TextWidget(fromTopRight(11, 0), Component.translatable("gui.arcanism.machine.buffer.label.available_storage")).noShadow());
		text2.addChild(new RectangleWidget(fromTopRight(2, 0), fromTopLeft(7, 7)).mainColor(0x3f000000));
		text2.addChild(new TextWidget(fromTopRight(11, 0), Component.translatable("gui.arcanism.machine.buffer.label.expandable_storage")).noShadow());

		// 帮助信息
		energyInfoWindow.addChild(new TextWidget(fromBottomRight(-5, -5), Component.literal("?")).noShadow().bottomAlignY().rightAlign().tooltip(Component.translatable("gui.arcanism.machine.buffer.tooltip.click_for_help"))
				.addClickBehavior(() -> addWidget(new MachineWidgets.TextWindow(Coordinate.fromCenter(0, 0), 150, Component.translatable("gui.arcanism.machine.buffer.help")))));

		energyInfoWindow.addChild(new RectangleWidget(fromBottomLeft(7, -18), fromTopRight(-13, 1)).mainColor(0x4fffffff));

		addWidget(new MachineWidgets.InventoryWindow(fromCenter(-200, 5), fromTopLeft(190, 100)));

		var chargeWindow = new MachineWidgets.InformationWindowWidget(fromCenter(60, -105), fromTopLeft(140, 100), Component.translatable("gui.arcanism.machine.window.charge"), 0, 0);
		addWidget(chargeWindow);

		chargeWindow.addChild(new OutlineWidget(fromCenter(0, -26), fromTopLeft(100, 28)).centerAlign());

		// slots
		menu.chargeSlotCount.whenFirstDataArrivesDo(() -> {
			var slotCount = menu.chargeSlotCount.get();
			for (int i = 0; i < slotCount; i++) {
				var x = -9 * slotCount + 1 + i * 18;

				chargeWindow.addChild(new RectangleWidget(fromCenter(x, -20), fromTopLeft(16, 16)).mainColor(0x1fffffff));
				chargeWindow.addChild(new SlotWidget(menu.slots.get(menu.chargeSlotStart + i), fromCenter(x, -20)));
			}
		});

		chargeWindow.addChild(new TextWidget(fromBottomLeft(7, -36), Component.translatable("gui.arcanism.machine.buffer.label.charge_power")).applyTheme(ThemeTemplate.BRIGHT_LABEL));
		chargeWindow.addChild(new TextWidget(fromBottomRight(-12, -36), Component.literal("W")).noShadow());
		chargeWindow.addChild(new NumberDisplayWidget(fromBottomRight(-49, -37), DynamicValue.fromSupplier(() -> menu.chargeRate.get() * menu.maxChargePower.get()), 6, 1, true));

		chargeWindow.addChild(new NumberInputWidget(Coordinate.fromBottomLeft(7, -21), fromTopRight(-14, 14), menu.chargeRate, 0, 1).setStep(0.01));
		chargeWindow.addChild(new ThinSlideBarWidget(fromBottomLeft(7, -25), fromTopRight(-14, 1), 0, 1, menu.chargeRate).step(0.01));

		var controlWindow = new MachineWidgets.InformationWindowWidget(fromCenter(0, 5), fromTopLeft(200, 100), Component.translatable("gui.arcanism.machine.window.machine_control"), 0, 0);
		addWidget(controlWindow);

		var expansionRatioText = controlWindow.addChild(new TextWidget(fromTopLeft(7, 21), Component.translatable("gui.arcanism.machine.buffer.label.expansion_ratio")).noShadow());
		expansionRatioText.addChild(new NumberDisplayWidget(Coordinate.fromTopRight(2, -5), DynamicValue.fromSupplier(() -> menu.powerFact.get() * 100), 3, 1.5, true));
		expansionRatioText.addChild(new TextWidget(fromTopRight(31, 0), Component.literal("%")).noShadow());

		var powerUnit = controlWindow.addChild(new TextSwitchWidget(fromTopRight(-6, 20), fromTopLeft(13, 9), 1, "W").rightAlign().bgColor(0));
		controlWindow.addChild(new NumberDisplayWidget(fromTopRight(-14, 16), DynamicValue.fromSupplier(() -> (Math.pow(11, menu.powerFact.get()) - 1) * menu.baseExpansionPower.get() / 10), 5, 1.5, (TextSwitchWidget) powerUnit, "W", true).rightAlign());
		powerUnit.addChild(new TextWidget(fromTopLeft(-48, 1), Component.translatable("gui.arcanism.machine.buffer.label.maintain_power")).noShadow().rightAlign());

		controlWindow.addChild(new RectangleWidget(fromTopLeft(7, 32), fromTopRight(-14, 1)).mainColor(0x3fffffff));

		var controlText1 = controlWindow.addChild(new TextWidget(fromCenterLeft(7, -10), Component.translatable("gui.arcanism.machine.buffer.label.base_capacity")).applyTheme(ThemeTemplate.BRIGHT_LABEL));
		controlText1.addChild(new NumberDisplayWidget(fromTopRight(2, -1), menu.baseStorage, 6, 1, true).mainColor(-1));
		controlText1.addChild(new TextWidget(fromTopRight(39, 0), Component.literal("J")).noShadow().mainColor(-1));
		var controlText2 = controlWindow.addChild(new TextWidget(fromCenterLeft(7, 2), Component.translatable("gui.arcanism.machine.buffer.label.max_capacity")).applyTheme(ThemeTemplate.BRIGHT_LABEL));
		controlText2.addChild(new NumberDisplayWidget(fromTopRight(2, -1), DynamicValue.fromSupplier(() -> menu.baseStorage.get() + menu.baseExpansion.get()), 6, 1, true).mainColor(-1));
		controlText2.addChild(new TextWidget(fromTopRight(39, 0), Component.literal("J")).noShadow().mainColor(-1));
		var controlText3 = controlWindow.addChild(new TextWidget(fromCenterRight(-6, -10), Component.literal("J")).noShadow().rightAlign());
		controlText3.addChild(new NumberDisplayWidget(fromTopLeft(-1, -1), DynamicValue.fromSupplier(() -> menu.baseStorage.get() + menu.powerFact.get() * menu.baseExpansion.get()), 6, 1, true).rightAlign());
		controlText3.addChild(new TextWidget(fromTopLeft(-38, 0), Component.translatable("gui.arcanism.machine.buffer.label.current_capacity")).applyTheme(ThemeTemplate.BRIGHT_LABEL).rightAlign());
		var controlText4 = controlWindow.addChild(new TextWidget(fromCenterRight(-6, 2), Component.literal("W")).noShadow().rightAlign());
		controlText4.addChild(new NumberDisplayWidget(fromTopLeft(-1, -1), DynamicValue.fromSupplier(() -> menu.baseExpansionPower.get()), 6, 1, true).rightAlign());
		controlText4.addChild(new TextWidget(fromTopLeft(-38, 0), Component.translatable("gui.arcanism.machine.buffer.label.max_power")).applyTheme(ThemeTemplate.BRIGHT_LABEL).rightAlign());

		controlWindow.addChild(new NumberInputWidget(Coordinate.fromBottomLeft(7, -21), fromTopRight(-14, 14), menu.powerFact, 0, 1).setStep(0.01));
		controlWindow.addChild(new ThinSlideBarWidget(fromBottomLeft(7, -25), fromTopRight(-14, 1), 0, 1, menu.powerFact).step(0.01));
	}
}
