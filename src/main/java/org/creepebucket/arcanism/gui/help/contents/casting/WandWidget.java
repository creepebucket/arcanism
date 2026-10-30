package org.creepebucket.arcanism.gui.help.contents.casting;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class WandWidget extends HelpWindowWidgets.HelpBodyWidget {
    public WandWidget() {
        super(Component.literal("魔杖"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("施法需要魔杖进行. 本条目将讲解魔杖的基本操作, 以及施法流程的简要说明."));
        addHeading("debugger_spell", Component.literal("认识魔杖"));
        addParagraph(Component.literal("在手持魔杖物品时按潜行+右键, 即可打开魔杖UI. 不过, 在施法之前, 你需要安装一些插件(关于插件的具体用法, 请见下一节), 魔杖的UI才会完全出现, 这个时候你就准备好进行施法了."));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand1.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addParagraph(Component.literal("当你打开魔杖UI的时候, 屏幕上应该会显示类似这样的画面, 以下是一些你需要关注的地方:"));
        addEntry("1_small", Component.literal("插件面板"), Component.literal("这是插件面板. 在这里安装你的插件, 已安装的插件也会在此处显示效果."));
        addEntry("2_small", Component.literal("物品栏"), Component.literal("这是你的物品栏. 背景偏白的部分为快捷栏."));
        addEntry("3_small", Component.literal("插件槽"), Component.literal("将插件放置在此处来安装插件."));
        addParagraph(Component.literal("一个能够完整施法的法杖需要以下插件各一个:"));
        addParagraph(Component.literal("法术供给/法术存储/法术释放"));
        addHeading("copy_to_spell", Component.literal("施法流程"));
        addParagraph(Component.literal("从以下操作开始施法流程:"));
        addEntry("1_small", Component.literal("GUI外"), Component.literal("手持魔杖右击(不要潜行)并按住;"));
        addEntry("2_small", Component.literal("GUI内"), Component.literal("点击并按住'发射法术'按钮."));
        addParagraph(Component.literal("在按住后, 法术将会开始充能, 你会在显眼的位置(鼠标tooltip/动作栏)看到充能提示, 样式为XXXJ, 这表示你当前已经为法术充能的能量值. 保持按住, 这个数值会增长, 增长速率由魔杖插件(见'魔杖插件')决定."));
        addParagraph(Component.literal("在松手后, 法术将被发射. 这将扣除你身上携带的魔力存储物品(见'认识魔力')上的魔力. 若法术未消耗全部魔力, 该部分魔力在法术执行结束后会返还."));
        addWarning(Component.literal("若你身上携带的魔力数量小于你的充能数值, 则只会将你的所有魔力充能给法术."));
        addHeading("compute_mod", Component.literal("法杖GUI"));
        addParagraph(Component.literal("在插件全部装好后, 再打开魔杖, UI将会呈现如下图所述的样式:"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/wand2.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.literal("法术面板"), Component.literal("用于供应和选择你需要的法术. 具体用法见'法术面板'."));
        addEntry("2_small", Component.literal("编辑面板"), Component.literal("你的法术序列将在这里存放和编辑. 具体用法见'法术编辑'."));
        addEntry("3_small", Component.literal("发射面板"), Component.literal("在这里控制法术的发射, 充能, 或调试器选项. 具体用法见'发射面板', 调试器功能见'调试器'."));
    }
}
