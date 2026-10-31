package org.creepebucket.arcanism.gui.help.contents.casting.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class SpellEditorWidget extends HelpWindowWidgets.HelpBodyWidget {
    public SpellEditorWidget() {
        super(Component.translatable("gui.arcanism.help.page.spell_editor.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.spell_editor.intro"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand4.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.spell_editor.entry.scroll_button.label"), Component.translatable("gui.arcanism.help.page.spell_editor.entry.scroll_button.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.spell_editor.entry.scrollbar.label"), Component.translatable("gui.arcanism.help.page.spell_editor.entry.scrollbar.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.spell_editor.entry.slots.label"), Component.translatable("gui.arcanism.help.page.spell_editor.entry.slots.note"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.spell_editor.entry.space.label"), Component.translatable("gui.arcanism.help.page.spell_editor.entry.space.note"));
        addNote(Component.translatable("gui.arcanism.help.page.spell_editor.note.space_hidden"));
        addEntry("5_small", Component.translatable("gui.arcanism.help.page.spell_editor.entry.global.label"), Component.translatable("gui.arcanism.help.page.spell_editor.entry.global.note"));
    }
}
