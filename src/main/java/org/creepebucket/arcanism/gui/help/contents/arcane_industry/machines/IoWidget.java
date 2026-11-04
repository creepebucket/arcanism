package org.creepebucket.arcanism.gui.help.contents.arcane_industry.machines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class IoWidget extends HelpWindowWidgets.HelpBodyWidget {
    public IoWidget() {
        super(Component.translatable("gui.arcanism.help.page.io.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.io.intro"));
        addImportant(Component.translatable("gui.arcanism.help.page.io.important.type_matching"));
        addHeading("items", Component.translatable("gui.arcanism.help.page.io.heading.items"));
        addParagraph(Component.translatable("gui.arcanism.help.page.io.paragraph.items_storage"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/machine2.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.io.entry.items_title.label"), Component.translatable("gui.arcanism.help.page.io.entry.items_title.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.io.entry.items_storage.label"), Component.translatable("gui.arcanism.help.page.io.entry.items_storage.note"));
        addHeading("fluids", Component.translatable("gui.arcanism.help.page.io.heading.fluids"));
        addParagraph(Component.translatable("gui.arcanism.help.page.io.paragraph.fluids_storage"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/machine3.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.io.entry.fluids_title.label"), Component.translatable("gui.arcanism.help.page.io.entry.fluids_title.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.io.entry.fluids_info.label"), Component.translatable("gui.arcanism.help.page.io.entry.fluids_info.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.io.entry.fluids_io.label"), Component.translatable("gui.arcanism.help.page.io.entry.fluids_io.note"));
        addEntry("4_small", Component.translatable("gui.arcanism.help.page.io.entry.fluids_amount.label"), Component.translatable("gui.arcanism.help.page.io.entry.fluids_amount.note"));
        addEntry("5_small", Component.translatable("gui.arcanism.help.page.io.entry.fluids_capacity.label"), Component.translatable("gui.arcanism.help.page.io.entry.fluids_capacity.note"));
    }
}
