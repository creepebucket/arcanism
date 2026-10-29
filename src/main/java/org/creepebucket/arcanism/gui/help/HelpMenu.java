package org.creepebucket.arcanism.gui.help;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import org.creepebucket.arcanism.gui.lib.ui.Menu;
import org.creepebucket.arcanism.registries.ModMenuTypes;

public class HelpMenu extends Menu {

	public HelpMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf extra) {
		this(containerId, playerInv);
	}

	public HelpMenu(int containerId, Inventory playerInv) {
		super(ModMenuTypes.HELP_MENU.get(), containerId, playerInv, InteractionHand.MAIN_HAND, Menu::init);
	}

	@Override
	public void init() {
	}
}
