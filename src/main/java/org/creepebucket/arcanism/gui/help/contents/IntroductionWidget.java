package org.creepebucket.arcanism.gui.help.contents;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class IntroductionWidget extends HelpWindowWidgets.HelpBodyWidget {
    public IntroductionWidget() {
        super(Component.literal("欢迎"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("欢迎使用 Arcanism 帮助页面."));
        addParagraph(Component.literal("本页将讲解帮助页面基本的使用方法, 以便您进行浏览. 鼠标滚轮向下滑动即可查看下方内容."));
        addHeading("directory", Component.literal("目录结构"));
        addParagraph(Component.literal("在左侧, 你可以选择帮助条目进行查看, 了解关于这个条目的内容, 下面简要介绍条目类型及含义:"));
        addEntry("small_arrow_right", Component.literal("条目组"), Component.literal("点击一个条目组会展开其包含的所有条目, 再次点击收起条目."));
        addEntry("small_arrow_in", Component.literal("条目"), Component.literal("这是帮助菜单的基础组成部分. 点击一个条目会打开对应条目, 当前打开的条目会加粗显示."));
        addNote(Component.literal("点击旁边的 '关于 Arcanism' 试试!"));
        addHeading("description", Component.literal("操作方式"));
        addParagraph(Component.literal("你可以在条目菜单框或帮助正文框使用鼠标滚轮来控制页面滚动."));
        addParagraph(Component.literal("如果页面在当前高度放不下, 屏幕右侧会出现白色滑动条, 按住并拖动也可以滚动页面."));
    }
}
