package org.creepebucket.arcanism.gui.help.contents.casting.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class CastPanelWidget extends HelpWindowWidgets.HelpBodyWidget {
    public CastPanelWidget() {
        super(Component.translatable("gui.arcanism.help.page.cast_panel.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.cast_panel.intro"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand5.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.cast_panel.entry.charge.label"), Component.translatable("gui.arcanism.help.page.cast_panel.entry.charge.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.cast_panel.entry.errors.label"), Component.translatable("gui.arcanism.help.page.cast_panel.entry.errors.note"));
        addImportant(Component.translatable("gui.arcanism.help.page.cast_panel.important.cant_cast"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.cast_panel.entry.bypass.label"), Component.translatable("gui.arcanism.help.page.cast_panel.entry.bypass.note"));
    }
}
