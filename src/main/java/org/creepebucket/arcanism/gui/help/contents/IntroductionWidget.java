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
        super("欢迎");
    }

    @Override
    public void buildContent() {
        addParagraph("欢迎来到《魔导主义》的帮助菜单。左侧是目录, 右侧是页面内容。本页介绍这个界面本身的用法。");

        addHeading("textures/gui/icons/directory.png", "目录导航");
        addEntry("textures/gui/icons/small_arrow_right.png", "折叠的条目", "点击展开, 显示它包含的子页面。");
        addEntry("textures/gui/icons/small_arrow_down.png", "展开的条目", "再次点击即可收起, 回到紧凑的列表。");
        addEntry("textures/gui/icons/small_arrow_in.png", "叶子条目", "点击后在右侧打开对应页面; 当前页面的名字在目录中加粗显示。");

        addHeading("textures/gui/icons/description.png", "阅读页面");
        addEntry("textures/gui/icons/small_arrow_up.png", "滚轮向上", "页面向顶部滚动; 鼠标悬停在左侧目录上时, 滚动的是目录。");
        addEntry("textures/gui/icons/small_arrow_down.png", "滚轮向下", "页面向底部滚动, 每次滚动一格。");

        var scrollbarRow = new Widget.BlankWidget(fromTopLeft(0, 0), fromTopRight(0, 12));
        scrollbarRow.addChild(new RectangleWidget(fromTopLeft(10, 2), fromTopLeft(4, 10)).mainColor(0x33ffffff));
        scrollbarRow.addChild(new RectangleWidget(fromTopLeft(10, 2), fromTopLeft(4, 4)).mainColor(ModColors.MAIN_COLOR_M));
        scrollbarRow.addChild(new TextWidget(fromTopLeft(22, 1), Component.literal("右侧滚动条")).noShadow().mainColor(0xffe8feff));
        acceptSegment(scrollbarRow);
        acceptSegment(new MultilineTextWidget(fromTopLeft(22, 0), fromTopRight(-30, 0), Component.literal("按住滑块上下拖动, 可以直接跳到页面的任意位置。页面放得下时, 滚动条会自动隐藏。")).lineHeight(11).noShadow().mainColor(0xff9a9a9a));

        addHeading("textures/gui/icons/nametag.png", "窗口与退出");
        addParagraph("调整游戏窗口大小后, 界面会自动重新排版, 不需要重新打开。");

        addNote("关闭菜单", "按 ESC 随时关闭帮助菜单。");
    }
}
