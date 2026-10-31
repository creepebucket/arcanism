package org.creepebucket.arcanism.gui.help.contents.casting.spell_syntax;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class SpellTooltipWidget extends HelpWindowWidgets.HelpBodyWidget {
    public SpellTooltipWidget() {
        super(Component.translatable("gui.arcanism.help.page.spell_tooltip.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.spell_tooltip.intro"));
        addHeading("description", Component.translatable("gui.arcanism.help.page.spell_tooltip.heading.structure"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.header.label"), Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.header.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.overloads.label"), Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.overloads.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.description.label"), Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.description.note"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.cost.label"), Component.translatable("gui.arcanism.help.page.spell_tooltip.entry.cost.note"));
        addNote(Component.translatable("gui.arcanism.help.page.spell_tooltip.note.overloads"));
    }
}
