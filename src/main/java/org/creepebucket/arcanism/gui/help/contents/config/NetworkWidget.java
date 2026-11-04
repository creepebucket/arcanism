package org.creepebucket.arcanism.gui.help.contents.config;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class NetworkWidget extends HelpWindowWidgets.HelpBodyWidget {
    public NetworkWidget() {
        super(Component.translatable("gui.arcanism.help.page.network.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.network.intro"));
        addHeading("import", Component.translatable("gui.arcanism.help.page.network.heading.interpolation"));
        addParagraph(Component.translatable("gui.arcanism.help.page.network.paragraph.interpolation"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.network.entry.client_side_interpolation.label"), Component.translatable("gui.arcanism.help.page.network.entry.client_side_interpolation.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.network.entry.client_interpolation_window.label"), Component.translatable("gui.arcanism.help.page.network.entry.client_interpolation_window.note"));
        addHeading("delay", Component.translatable("gui.arcanism.help.page.network.heading.sync_interval"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.network.entry.server_data_sync_interval.label"), Component.translatable("gui.arcanism.help.page.network.entry.server_data_sync_interval.note"));
        addImportant(Component.translatable("gui.arcanism.help.page.network.important.difference"));
    }
}
