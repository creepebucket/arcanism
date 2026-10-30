package org.creepebucket.arcanism.gui.help.contents;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class IntroductionWidget extends HelpWindowWidgets.HelpBodyWidget {
    public IntroductionWidget() {
        super(Component.translatable("gui.arcanism.help.page.welcome.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.welcome.intro"));
        addParagraph(Component.translatable("gui.arcanism.help.page.welcome.usage"));
        addHeading("directory", Component.translatable("gui.arcanism.help.page.welcome.heading.directory"));
        addParagraph(Component.translatable("gui.arcanism.help.page.welcome.directory_intro"));
        addEntry("small_arrow_right", Component.translatable("gui.arcanism.help.page.welcome.entry.group.label"), Component.translatable("gui.arcanism.help.page.welcome.entry.group.note"));
        addEntry("small_arrow_in", Component.translatable("gui.arcanism.help.page.welcome.entry.leaf.label"), Component.translatable("gui.arcanism.help.page.welcome.entry.leaf.note"));
        addNote(Component.translatable("gui.arcanism.help.page.welcome.note.about"));
        addHeading("description", Component.translatable("gui.arcanism.help.page.welcome.heading.controls"));
        addParagraph(Component.translatable("gui.arcanism.help.page.welcome.controls.scroll"));
        addParagraph(Component.translatable("gui.arcanism.help.page.welcome.controls.scrollbar"));
    }
}
