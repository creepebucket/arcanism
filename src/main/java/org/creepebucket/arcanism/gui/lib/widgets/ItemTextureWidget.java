package org.creepebucket.arcanism.gui.lib.widgets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.gui.lib.api.widgets.Renderable;

public class ItemTextureWidget extends Widget implements Renderable {
	public String itemId;

	public ItemTextureWidget(Coordinate pos, Coordinate size, String itemId) {
		super(pos, size);
		this.itemId = itemId;
	}

	@Override
	public void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		if (itemId.isEmpty()) return;

		BuiltInRegistries.ITEM.get(Identifier.parse(itemId))
				.map(holder -> new ItemStack(holder.value()))
				.ifPresent(stack -> graphics.item(stack, x(), y()));
	}
}
