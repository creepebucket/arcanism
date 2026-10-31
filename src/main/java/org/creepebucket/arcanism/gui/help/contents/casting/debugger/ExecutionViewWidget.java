package org.creepebucket.arcanism.gui.help.contents.casting.debugger;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class ExecutionViewWidget extends HelpWindowWidgets.HelpBodyWidget {
    public ExecutionViewWidget() {
        super(Component.translatable("gui.arcanism.help.page.execution_view.title"));
    }

    @Override
    public void buildContent() {
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand7.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addParagraph(Component.translatable("gui.arcanism.help.page.execution_view.current"));
    }
}
