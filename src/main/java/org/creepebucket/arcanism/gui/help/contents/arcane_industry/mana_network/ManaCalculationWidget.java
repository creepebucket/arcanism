package org.creepebucket.arcanism.gui.help.contents.arcane_industry.mana_network;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class ManaCalculationWidget extends HelpWindowWidgets.HelpBodyWidget {
    public ManaCalculationWidget() {
        super(Component.translatable("gui.arcanism.help.page.mana_calculation.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.tick"));

        addHeading("momentum", Component.translatable("gui.arcanism.help.page.mana_calculation.heading.load"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.load"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.load_watt"));

        addHeading("backpack", Component.translatable("gui.arcanism.help.page.mana_calculation.heading.capacity"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.capacity"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.capacity_zero"));

        addHeading("radiation", Component.translatable("gui.arcanism.help.page.mana_calculation.heading.settlement"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.settlement"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.four_types"));

        addHeading("compute_mod", Component.translatable("gui.arcanism.help.page.mana_calculation.heading.can_run"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.can_run"));
        addParagraph(Component.translatable("gui.arcanism.help.page.mana_calculation.paragraph.no_switch"));

        addNote(Component.translatable("gui.arcanism.help.page.mana_calculation.note.sync_interval"));
        addWarning(Component.translatable("gui.arcanism.help.page.mana_calculation.warning.cleanup"));
    }
}
