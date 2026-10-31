package org.creepebucket.arcanism.gui.help.contents.casting.debugger;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class SpellControlWidget extends HelpWindowWidgets.HelpBodyWidget {
    public SpellControlWidget() {
        super(Component.translatable("gui.arcanism.help.page.spell_control.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.spell_control.intro"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand8.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.spell_control.entry.toggle.label"), Component.translatable("gui.arcanism.help.page.spell_control.entry.toggle.note"));
        addWarning(Component.translatable("gui.arcanism.help.page.spell_control.warning.manual"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.spell_control.entry.step.label"), Component.translatable("gui.arcanism.help.page.spell_control.entry.step.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.spell_control.entry.tick.label"), Component.translatable("gui.arcanism.help.page.spell_control.entry.tick.note"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.spell_control.entry.resume.label"), Component.translatable("gui.arcanism.help.page.spell_control.entry.resume.note"));
        addEntry("5_small", Component.translatable("gui.arcanism.help.page.spell_control.entry.pause.label"), Component.translatable("gui.arcanism.help.page.spell_control.entry.pause.note"));
        addImportant(Component.translatable("gui.arcanism.help.page.spell_control.important.close_ui"));
    }
}
