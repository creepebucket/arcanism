package org.creepebucket.arcanism.gui.help.contents.arcane_industry.machines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class MachineOverviewWidget extends HelpWindowWidgets.HelpBodyWidget {
    public MachineOverviewWidget() {
        super(Component.literal("机器总览"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("机器可以用来产生魔力, 或者使用魔力来加工物品."));
        addParagraph(Component.literal("当你手持一个机器物品时, 游戏会显示一个线框, 表示机器摆放位置的预览. 若在此位置无法摆放, 线框将显示红色."));
        addParagraph(Component.literal("在机器被放下后, 会自动占据一些空气方块填充其内部."));
        addHeading("debugger_spell", Component.literal("GUI 系统"));
        addParagraph(Component.literal("右键可以打开机器主要GUI, 对着I/O端口潜行右键可以打开当前I/O端口的GUI."));
        addParagraph(Component.literal("机器的GUI系统由模块化的窗口组成, 单个窗口大致表现为这样:"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/machine1.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.literal("标题栏"), Component.literal("按住并拖动可以移动窗口位置."));
        addEntry("2_small", Component.literal("关闭按钮"), Component.literal("点击关闭这个窗口."));
        addEntry("3_small", Component.literal("窗口右下角"), Component.literal("按住并拖动可以改变窗口大小."));
        addParagraph(Component.literal("窗口的内容会随窗口宽高而实时改变, 所以你可以定制自己所喜好的布局."));
        addParagraph(Component.literal("若窗口被意外关闭, 按下Alt+W可以打开窗口管理页面, 开关所有可用窗口."));
    }
}
