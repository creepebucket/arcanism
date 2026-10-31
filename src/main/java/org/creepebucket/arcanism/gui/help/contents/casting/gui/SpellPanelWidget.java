package org.creepebucket.arcanism.gui.help.contents.casting.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class SpellPanelWidget extends HelpWindowWidgets.HelpBodyWidget {
    public SpellPanelWidget() {
        super(Component.translatable("gui.arcanism.help.page.spell_panel.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.spell_panel.intro"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand3.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.spell_panel.entry.category_jump.label"), Component.translatable("gui.arcanism.help.page.spell_panel.entry.category_jump.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.spell_panel.entry.category_title.label"), Component.translatable("gui.arcanism.help.page.spell_panel.entry.category_title.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.spell_panel.entry.supply_slots.label"), Component.translatable("gui.arcanism.help.page.spell_panel.entry.supply_slots.note"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.spell_panel.entry.scrollbar.label"), Component.translatable("gui.arcanism.help.page.spell_panel.entry.scrollbar.note"));
        addNote(Component.translatable("gui.arcanism.help.page.spell_panel.note.scroll"));
    }
}
