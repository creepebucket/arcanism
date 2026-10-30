package org.creepebucket.arcanism.gui.help.contents;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class WhatIsManaWidget extends HelpWindowWidgets.HelpBodyWidget {
    public WhatIsManaWidget() {
        super(Component.translatable("gui.arcanism.help.page.what_is_mana.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.what_is_mana.intro"));
        addHeading("debugger_spell", Component.translatable("gui.arcanism.help.page.what_is_mana.heading.ubiquitous"));
        addParagraph(Component.translatable("gui.arcanism.help.page.what_is_mana.ubiquitous.intro"));
        addEntry("radiation_small",   Component.translatable("gui.arcanism.help.page.what_is_mana.entry.radiation.label"), Component.translatable("gui.arcanism.help.page.what_is_mana.entry.radiation.note"));
        addEntry("temperature_small", Component.translatable("gui.arcanism.help.page.what_is_mana.entry.temperature.label"), Component.translatable("gui.arcanism.help.page.what_is_mana.entry.temperature.note"));
        addEntry("momentum_small",    Component.translatable("gui.arcanism.help.page.what_is_mana.entry.momentum.label"), Component.translatable("gui.arcanism.help.page.what_is_mana.entry.momentum.note"));
        addEntry("pressure_small",    Component.translatable("gui.arcanism.help.page.what_is_mana.entry.pressure.label"), Component.translatable("gui.arcanism.help.page.what_is_mana.entry.pressure.note"));
        addHeading("backpack", Component.translatable("gui.arcanism.help.page.what_is_mana.heading.storage"));
        addParagraph(Component.translatable("gui.arcanism.help.page.what_is_mana.storage.collect"));
        addParagraph(Component.translatable("gui.arcanism.help.page.what_is_mana.storage.container"));
        addParagraph(Component.translatable("gui.arcanism.help.page.what_is_mana.storage.examples"));
    }
}
