package org.creepebucket.arcanism.gui.help.contents;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.gui.lib.widgets.MultilineTextWidget;
import org.creepebucket.arcanism.gui.lib.widgets.RectangleWidget;
import org.creepebucket.arcanism.gui.lib.widgets.TextWidget;
import org.creepebucket.arcanism.utils.ModColors;

import static org.creepebucket.arcanism.gui.lib.api.Coordinate.*;

public class IntroductionWidget extends HelpWindowWidgets.HelpBodyWidget {
    public IntroductionWidget() {
        super("gui.arcanism.help.page.welcome.title");
    }

    @Override
    public void buildContent() {
        addParagraph("gui.arcanism.help.page.welcome.intro");

        addHeading("textures/gui/icons/directory.png", "gui.arcanism.help.page.welcome.heading.directory");
        addEntry("textures/gui/icons/small_arrow_right.png", "gui.arcanism.help.page.welcome.entry.expand.label", "gui.arcanism.help.page.welcome.entry.expand.note");
        addEntry("textures/gui/icons/small_arrow_down.png", "gui.arcanism.help.page.welcome.entry.collapse.label", "gui.arcanism.help.page.welcome.entry.collapse.note");
        addEntry("textures/gui/icons/small_arrow_in.png", "gui.arcanism.help.page.welcome.entry.leaf.label", "gui.arcanism.help.page.welcome.entry.leaf.note");

        addHeading("textures/gui/icons/description.png", "gui.arcanism.help.page.welcome.heading.reading");
        addEntry("textures/gui/icons/small_arrow_up.png", "gui.arcanism.help.page.welcome.entry.scroll_up.label", "gui.arcanism.help.page.welcome.entry.scroll_up.note");
        addEntry("textures/gui/icons/small_arrow_down.png", "gui.arcanism.help.page.welcome.entry.scroll_down.label", "gui.arcanism.help.page.welcome.entry.scroll_down.note");

        var scrollbarRow = new Widget.BlankWidget(fromTopLeft(0, 0), fromTopRight(0, 12));
        scrollbarRow.addChild(new RectangleWidget(fromTopLeft(10, 2), fromTopLeft(4, 10)).mainColor(0x33ffffff));
        scrollbarRow.addChild(new RectangleWidget(fromTopLeft(10, 2), fromTopLeft(4, 4)).mainColor(ModColors.MAIN_COLOR_M));
        scrollbarRow.addChild(new TextWidget(fromTopLeft(22, 1), Component.translatable("gui.arcanism.help.page.welcome.scrollbar.label")).noShadow().mainColor(0xffe8feff));
        acceptSegment(scrollbarRow);
        acceptSegment(new MultilineTextWidget(fromTopLeft(22, 0), fromTopRight(-30, 0), Component.translatable("gui.arcanism.help.page.welcome.scrollbar.note")).lineHeight(11).noShadow().mainColor(0xff9a9a9a));

        addHeading("textures/gui/icons/nametag.png", "gui.arcanism.help.page.welcome.heading.window");
        addParagraph("gui.arcanism.help.page.welcome.window.resize");

        addNote("gui.arcanism.help.page.welcome.note.close.title", "gui.arcanism.help.page.welcome.note.close.text");
    }
}
