package org.creepebucket.arcanism.gui.machines.buffer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.api.DynamicValue;
import org.creepebucket.arcanism.gui.lib.widgets.NumberDisplayWidget;
import org.creepebucket.arcanism.gui.lib.widgets.ProgressBarWidget;
import org.creepebucket.arcanism.gui.lib.widgets.RectangleWidget;
import org.creepebucket.arcanism.gui.lib.widgets.TextWidget;
import org.creepebucket.arcanism.gui.machines.api.MachineScreen;
import org.creepebucket.arcanism.gui.machines.api.MachineWidgets;
import org.creepebucket.arcanism.utils.ModColors;

import static org.creepebucket.arcanism.gui.lib.api.Coordinate.*;

public class ManaBufferScreen extends MachineScreen<ManaBufferMenu> {

	public ManaBufferScreen(ManaBufferMenu menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}

	@Override
	public void buildWidget() {
		super.buildWidget();

		var energyInfoWindow = new MachineWidgets.InformationWindowWidget(fromCenter(-200, -100), fromTopLeft(300, 100), Component.translatable("gui.arcanism.machine.buffer.window.mana_info"), 0, 0);
		addWidget(energyInfoWindow);


		// 需要等待数据回传
		menu.baseStorage.whenFirstDataArrivesDo(() -> {

			var expansionRatio = menu.baseStorage.get() / (menu.baseExpansion.get() + menu.baseStorage.get());

			// r
			energyInfoWindow.addChild(new ProgressBarWidget(custom(expansionRatio, 0, 0, 19), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(2.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoWindow.addChild(new ProgressBarWidget(fromTopLeft(54, 19), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.radiationStorageJ,
					DynamicValue.fromSupplier(() -> menu.radiationCacheJ.get() / (1 + expansionRatio)))).mainColor(ModColors.MAIN_COLOR_R).bgColor(0x1fffffff);

			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopLeft(7, 19), menu.radiationStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_R));
			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopRight(-8, 19), menu.radiationCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_R).rightAlign());

			// t
			energyInfoWindow.addChild(new ProgressBarWidget(custom(expansionRatio, 0, 0, 31), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(2.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoWindow.addChild(new ProgressBarWidget(fromTopLeft(54, 31), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.temperatureStorageJ,
					DynamicValue.fromSupplier(() -> menu.temperatureCacheJ.get() / (1 + expansionRatio)))).mainColor(ModColors.MAIN_COLOR_T).bgColor(0x1fffffff);

			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopLeft(7, 31), menu.temperatureStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_T));
			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopRight(-8, 31), menu.temperatureCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_T).rightAlign());

			// m
			energyInfoWindow.addChild(new ProgressBarWidget(custom(expansionRatio, 0, 0, 43), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(2.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoWindow.addChild(new ProgressBarWidget(fromTopLeft(54, 43), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.momentumStorageJ,
					DynamicValue.fromSupplier(() -> menu.momentumCacheJ.get() / (1 + expansionRatio)))).mainColor(ModColors.MAIN_COLOR_M).bgColor(0x1fffffff);

			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopLeft(7, 43), menu.momentumStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_M));
			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopRight(-8, 43), menu.momentumCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_M).rightAlign());

			// p
			energyInfoWindow.addChild(new ProgressBarWidget(custom(expansionRatio, 0, 0, 55), custom(1 - expansionRatio, -108 * (1 - expansionRatio), 0, 9),
					menu.powerFact, DynamicValue.staticValue(2.0)).hideText()).mainColor(0x1fffffff).bgColor(0x3f000000);
			energyInfoWindow.addChild(new ProgressBarWidget(fromTopLeft(54, 55), custom(expansionRatio, -108 * expansionRatio, 0, 9), menu.pressureStorageJ,
					DynamicValue.fromSupplier(() -> menu.pressureCacheJ.get() / (1 + expansionRatio)))).mainColor(ModColors.MAIN_COLOR_P).bgColor(0x1fffffff);

			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopLeft(7, 55), menu.pressureStorageJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_P));
			energyInfoWindow.addChild(new NumberDisplayWidget(fromTopRight(-8, 55), menu.pressureCacheJ, 7, 1, true).mainColor(ModColors.MAIN_COLOR_P).rightAlign());

		});

		// 图例
		energyInfoWindow.addChild(new RectangleWidget(fromBottomLeft(7, -7), fromTopLeft(7, 7)).mainColor(-1).bottomAlignY());
		var text1 = energyInfoWindow.addChild(new TextWidget(fromBottomLeft(16, -5), Component.translatable("gui.arcanism.machine.buffer.label.stored_mana")).noShadow().bottomAlignY());
		text1.addChild(new RectangleWidget(fromTopRight(2, 0), fromTopLeft(7, 7)).mainColor(0x1fffffff));
		var text2 = text1.addChild(new TextWidget(fromTopRight(11, 0), Component.translatable("gui.arcanism.machine.buffer.label.available_storage")));
		text2.addChild(new RectangleWidget(fromTopRight(2, 0), fromTopLeft(7, 7)).mainColor(0x3f000000));
		text2.addChild(new TextWidget(fromTopRight(11, 0), Component.translatable("gui.arcanism.machine.buffer.label.expandable_storage")));
		addWidget(new MachineWidgets.InventoryWindow(fromCenter(-90, 135), fromTopLeft(184, 110)));

		// 帮助信息
		energyInfoWindow.addChild(new TextWidget(fromBottomRight(-5, -5), Component.literal("?")).noShadow().bottomAlignY().rightAlign().tooltip(Component.translatable("gui.arcanism.machine.buffer.tooltip.click_for_help"))
				.addClickBehavior(() -> addWidget(new MachineWidgets.TextWindow(Coordinate.fromCenter(0, 0), 150, Component.translatable("gui.arcanism.machine.buffer.help")))));

		energyInfoWindow.addChild(new RectangleWidget(fromBottomLeft(7, -18), fromTopRight(-13, 1)).mainColor(0x4fffffff));
	}
}
