package org.creepebucket.arcanism.gui.help.contents.arcane_industry.mana_network;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class NetworkOverviewWidget extends HelpWindowWidgets.HelpBodyWidget {
    public NetworkOverviewWidget() {
        super(Component.translatable("gui.arcanism.help.page.network_overview.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.network_overview.paragraph.network"));

        addHeading("compile", Component.translatable("gui.arcanism.help.page.network_overview.heading.connect"));
        addParagraph(Component.translatable("gui.arcanism.help.page.network_overview.paragraph.connect"));
        addParagraph(Component.translatable("gui.arcanism.help.page.network_overview.paragraph.disconnect"));

        addHeading("directory", Component.translatable("gui.arcanism.help.page.network_overview.heading.merge"));
        addParagraph(Component.translatable("gui.arcanism.help.page.network_overview.paragraph.merge"));
        addParagraph(Component.translatable("gui.arcanism.help.page.network_overview.paragraph.split"));

        addHeading("information", Component.translatable("gui.arcanism.help.page.network_overview.heading.inspect"));
        addParagraph(Component.translatable("gui.arcanism.help.page.network_overview.paragraph.inspect"));
        addParagraph(Component.translatable("gui.arcanism.help.page.network_overview.paragraph.admin_commands"));

        addNote(Component.translatable("gui.arcanism.help.page.network_overview.note.id"));
        addImportant(Component.translatable("gui.arcanism.help.page.network_overview.important.cleanup"));
    }
}
