package org.creepebucket.arcanism.gui.help.contents.arcane_industry.mana_network;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class NodeTypesWidget extends HelpWindowWidgets.HelpBodyWidget {
    public NodeTypesWidget() {
        super(Component.translatable("gui.arcanism.help.page.node_types.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.node_types.paragraph.intro"));

        addEntry("directory", Component.translatable("gui.arcanism.help.page.node_types.entry.basic.label"), Component.translatable("gui.arcanism.help.page.node_types.entry.basic.note"));
        addEntry("directory", Component.translatable("gui.arcanism.help.page.node_types.entry.horizontal.label"), Component.translatable("gui.arcanism.help.page.node_types.entry.horizontal.note"));
        addEntry("directory", Component.translatable("gui.arcanism.help.page.node_types.entry.vertical.label"), Component.translatable("gui.arcanism.help.page.node_types.entry.vertical.note"));

        addHeading("warning", Component.translatable("gui.arcanism.help.page.node_types.heading.faces"));
        addParagraph(Component.translatable("gui.arcanism.help.page.node_types.paragraph.faces"));
        addParagraph(Component.translatable("gui.arcanism.help.page.node_types.paragraph.faces_vertical"));

        addHeading("information", Component.translatable("gui.arcanism.help.page.node_types.heading.machine"));
        addParagraph(Component.translatable("gui.arcanism.help.page.node_types.paragraph.machine_ports"));
        addParagraph(Component.translatable("gui.arcanism.help.page.node_types.paragraph.machine_auto"));

        addNote(Component.translatable("gui.arcanism.help.page.node_types.note.no_consumption"));
    }
}
