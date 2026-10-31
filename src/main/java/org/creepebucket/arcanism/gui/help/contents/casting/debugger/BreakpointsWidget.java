package org.creepebucket.arcanism.gui.help.contents.casting.debugger;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class BreakpointsWidget extends HelpWindowWidgets.HelpBodyWidget {
    public BreakpointsWidget() {
        super(Component.translatable("gui.arcanism.help.page.breakpoints.title"));
    }

    @Override
    public void buildContent() {
        addWarning(Component.translatable("gui.arcanism.help.page.breakpoints.warning.debugger"));
        addParagraph(Component.translatable("gui.arcanism.help.page.breakpoints.intro"));
        addNote(Component.translatable("gui.arcanism.help.page.breakpoints.note.skipped"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand6.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addParagraph(Component.translatable("gui.arcanism.help.page.breakpoints.place"));
        addImportant(Component.translatable("gui.arcanism.help.page.breakpoints.important.live_change"));
    }
}
