package org.creepebucket.arcanism.gui.help.contents.config;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class GameplayWidget extends HelpWindowWidgets.HelpBodyWidget {
    public GameplayWidget() {
        super(Component.translatable("gui.arcanism.help.page.gameplay.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.gameplay.intro"));
        addHeading("temperature", Component.translatable("gui.arcanism.help.page.gameplay.heading.fuel"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.fuel_value_multiplier.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.fuel_value_multiplier.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.more_balanced_fuel.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.more_balanced_fuel.note"));
        addHeading("compute_mod", Component.translatable("gui.arcanism.help.page.gameplay.heading.cast_cost"));
        addParagraph(Component.translatable("gui.arcanism.help.page.gameplay.paragraph.cast_cost_intro"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.cast_mass.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.cast_mass.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.break_energy_per_hardness.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.break_energy_per_hardness.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.global_cast_cost_mult.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.global_cast_cost_mult.note"));
        addHeading("directory", Component.translatable("gui.arcanism.help.page.gameplay.heading.mana_net_split"));
        addParagraph(Component.translatable("gui.arcanism.help.page.gameplay.paragraph.mana_net_split_intro"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.split_half.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.split_half.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.split_node_ratio.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.split_node_ratio.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.split_ratio.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.split_ratio.note"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.gameplay.entry.split_largest.label"), Component.translatable("gui.arcanism.help.page.gameplay.entry.split_largest.note"));
    }
}
