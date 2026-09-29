package org.creepebucket.arcanism.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.creepebucket.arcanism.ModConfig;
import org.creepebucket.arcanism.events.machines.BasicMachineTooltip;
import org.creepebucket.arcanism.gui.lib.api.Color;
import org.creepebucket.arcanism.items.api.ModItemExtensions;
import org.creepebucket.arcanism.mananet.machines.BasicMachine;
import org.creepebucket.arcanism.registries.ModDataComponents;
import org.creepebucket.arcanism.registries.WandPluginRegistry;
import org.creepebucket.arcanism.utils.ModColors;
import org.creepebucket.arcanism.utils.ModUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;

import static org.creepebucket.arcanism.Arcanism.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ItemTooltipHandler {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        var window = Minecraft.getInstance().getWindow();
        // 对于所有魔杖（Wand），永远在底部追加绿色属性说明
        boolean ctrl = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_CONTROL);
        boolean shift = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
        boolean alt = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_ALT);
        boolean tabKey = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_TAB);

        var level = Minecraft.getInstance().level;
        if (level != null) {
            int burnTime = event.getItemStack().getBurnTime(RecipeType.SMELTING, level.fuelValues());
            if (burnTime > 0)
                event.getToolTip().add(Component.translatable("tooltip.arcanism.heat_value",
                    ModUtils.formattedNumber(burnTime * ModConfig.CONFIG.fuelValueMultiplier.get())));
        }

        var storedMana = event.getItemStack().get(ModDataComponents.MANA.get());
        if (storedMana != null) {
            appendManaRow(event.getToolTip(), "R", storedMana.current.getRadiation(), storedMana.capacity.getRadiation(), ModColors.MAIN_COLOR_R);
            appendManaRow(event.getToolTip(), "T", storedMana.current.getTemperature(), storedMana.capacity.getTemperature(), ModColors.MAIN_COLOR_T);
            appendManaRow(event.getToolTip(), "M", storedMana.current.getMomentum(), storedMana.capacity.getMomentum(), ModColors.MAIN_COLOR_M);
            appendManaRow(event.getToolTip(), "P", storedMana.current.getPressure(), storedMana.capacity.getPressure(), ModColors.MAIN_COLOR_P);
        }

        if (event.getItemStack().getItem() instanceof ModItemExtensions ext) {
            ext.appendTooltip(event.getItemStack(), event.getToolTip(), ctrl, shift, alt);
            return;
        }

        var item = event.getItemStack().getItem();
        if (item instanceof BlockItem bi && bi.getBlock() instanceof BasicMachine machine) {
            BasicMachineTooltip.append(event.getItemStack(), event.getToolTip(), machine, ctrl, shift, alt, tabKey);
            return;
        }

        if (WandPluginRegistry.isPlugin(item)) {
            WandPluginRegistry.getPlugin(item).appendTooltip(event.getItemStack(), event.getToolTip(), ctrl, shift, alt);
        }
    }

    public static void appendManaRow(List<Component> tooltip, String key, double current, double capacity, Color color) {
        if (capacity <= 0) return;

        int barLength = 36;
        int filled = (int) Math.round(current / capacity * barLength);
        int argb = color.toArgb();

        var row = Component.literal(key + " " + ModUtils.FormattedManaString(current) + " ").withColor(argb);
        row.append(Component.literal("|".repeat(filled)).withColor(argb));
        row.append(Component.literal("|".repeat(barLength - filled)).withColor(0xFF808080));
        row.append(Component.literal(" " + ModUtils.FormattedManaString(capacity) + " (" + String.format("%.1f", current / capacity * 100) + "%)").withColor(argb));
        tooltip.add(row);
    }
}
