package org.creepebucket.arcanism.gui.help.contents.casting;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class WandPluginsWidget extends HelpWindowWidgets.HelpBodyWidget{
    public WandPluginsWidget() {
        super(Component.translatable("gui.arcanism.help.page.wand_plugins.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.wand_plugins.intro"));
        addHeading("wand_plugins", Component.translatable("gui.arcanism.help.page.wand_plugins.heading.overview"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand_plugins.overview.intro"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.supply.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.supply.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.storage.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.storage.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.release.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.release.note"));
        addNote(Component.translatable("gui.arcanism.help.page.wand_plugins.note.disabled_ui"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand_plugins.optional.intro"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.packing.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.packing.note"));
        addEntry("5_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.theme.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.theme.note"));
        addHeading("spell_count", Component.translatable("gui.arcanism.help.page.wand_plugins.heading.levels"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand_plugins.levels.intro"));
        addHeading("description", Component.translatable("gui.arcanism.help.page.wand_plugins.heading.data"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand_plugins.data.intro"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.mana_modifier.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.mana_modifier.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.slots.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.slots.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.charge_rate.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.charge_rate.note"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.wand_plugins.entry.plugin_slots.label"), Component.translatable("gui.arcanism.help.page.wand_plugins.entry.plugin_slots.note"));
    }
}
