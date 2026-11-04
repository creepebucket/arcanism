package org.creepebucket.arcanism.gui.help.contents.config;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class VisualsWidget extends HelpWindowWidgets.HelpBodyWidget {
    public VisualsWidget() {
        super(Component.translatable("gui.arcanism.help.page.visuals.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.visuals.intro"));
        addHeading("description", Component.translatable("gui.arcanism.help.page.visuals.heading.animation"));
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.visuals.entry.disable_animations.label"), Component.translatable("gui.arcanism.help.page.visuals.entry.disable_animations.note"));
        addNote(Component.translatable("gui.arcanism.help.page.visuals.note.client_only"));
    }
}
