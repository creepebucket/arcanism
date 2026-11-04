package org.creepebucket.arcanism.gui.help.contents.arcane_industry.machines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class IoWidget extends HelpWindowWidgets.HelpBodyWidget {
    public IoWidget() {
        super(Component.literal("输入/输出"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("I/O端口通常在机器中表现为突出的管道, 同时与外界的接触面为紫色. 对准端口方块潜行右键可以打开端口方块的GUI."));
        addImportant(Component.literal("对应类型的端口只接受外部对应类型的操作. 例如: 一个物品输入端口只能从外部(如管道)插入物品而无法取出物品."));
        addHeading("items", Component.literal("物品端口"));
        addParagraph(Component.literal("物品端口拥有16格的存储空间. 其UI如下"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/machine2.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.literal("窗口标题"), Component.literal("指示该端口的出入方向."));
        addEntry("2_small", Component.literal("端口存储"), Component.literal("端口的16格物品存储."));
        addHeading("fluids", Component.literal("流体端口"));
        addParagraph(Component.literal("流体端口能存储16KL流体, 其UI如下:"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/machine3.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.literal("窗口标题"), Component.literal("指示该端口的出入方向."));
        addEntry("2_small", Component.literal("流体信息"), Component.literal("用于指示当前存储的是何种流体."));
        addEntry("3_small", Component.literal("I/O端口"), Component.literal("放置能盛装流体的容器, 将其填满或排空. 你可以在左侧格子放置流体容器."));
        addEntry("4_small", Component.literal("存储显示"), Component.literal("显示当前存储流体数量."));
        addEntry("5_small", Component.literal("容量进度条"), Component.literal("显示当前容量和百分比容量."));
    }
}
