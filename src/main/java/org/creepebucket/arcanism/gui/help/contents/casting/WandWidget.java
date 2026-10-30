package org.creepebucket.arcanism.gui.help.contents.casting;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class WandWidget extends HelpWindowWidgets.HelpBodyWidget {
    public WandWidget() {
        super(Component.translatable("gui.arcanism.help.page.wand.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.intro"));
        addHeading("debugger_spell", Component.translatable("gui.arcanism.help.page.wand.heading.overview"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.overview.intro"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand1.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.overview.caption"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.wand.entry.plugins.label"), Component.translatable("gui.arcanism.help.page.wand.entry.plugins.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.wand.entry.inventory.label"), Component.translatable("gui.arcanism.help.page.wand.entry.inventory.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.wand.entry.slots.label"), Component.translatable("gui.arcanism.help.page.wand.entry.slots.note"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.requirements.intro"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.requirements.list"));
        addHeading("copy_to_spell", Component.translatable("gui.arcanism.help.page.wand.heading.casting"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.casting.intro"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.wand.entry.outside.label"), Component.translatable("gui.arcanism.help.page.wand.entry.outside.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.wand.entry.inside.label"), Component.translatable("gui.arcanism.help.page.wand.entry.inside.note"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.casting.charge"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.casting.release"));
        addWarning(Component.translatable("gui.arcanism.help.page.wand.warning.mana"));
        addHeading("compute_mod", Component.translatable("gui.arcanism.help.page.wand.heading.gui"));
        addParagraph(Component.translatable("gui.arcanism.help.page.wand.gui.intro"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand2.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.wand.entry.spell_panel.label"), Component.translatable("gui.arcanism.help.page.wand.entry.spell_panel.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.wand.entry.editor.label"), Component.translatable("gui.arcanism.help.page.wand.entry.editor.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.wand.entry.cast_panel.label"), Component.translatable("gui.arcanism.help.page.wand.entry.cast_panel.note"));
    }
}
