package org.creepebucket.arcanism.gui.help.contents;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class WhatIsManaWidget extends HelpWindowWidgets.HelpBodyWidget {
    public WhatIsManaWidget() {
        super(Component.literal("认识魔力"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("魔力是 Arcanism 玩法的基石, 在本模组一切操作均依赖于魔力. 本条目将简要介绍魔力, 其种类及其用途."));
        addHeading("debugger_spell", Component.literal("无处不在的魔力"));
        addParagraph(Component.literal("魔力存储在魔力单元等存储设备中. 魔力在释放时会对周围物理环境发生改变. 魔力的类型是依据于其在释放时所改变的环境物理特性所导致的, 主要分为4种:"));
        addEntry("radiation_small",   Component.literal("辐射"), Component.literal("这种魔力在被释放时会发出耀眼的光芒. 你会在需要发光的场景中使用该魔力. 同时, 光可以提升魔导材料的亲和度, 使机器能够运行得更快或能存储更多魔力."));
        addEntry("temperature_small", Component.literal("温度"), Component.literal("这种魔力在被释放时会加热周围物体. 如果需要加热某物, 或者施放设计到放热过程的法术, 你会需要它."));
        addEntry("momentum_small",    Component.literal("动量"), Component.literal("这种魔力在被释放时会给周围物体一定动能. 在移动物体的机器或法术中用途广泛."));
        addEntry("pressure_small",    Component.literal("压力"), Component.literal("这种魔力在被释放时会压缩周围的物体. 通常出现在破碎, 塑型等场合. 也会在需要挤压作用的法术中使用."));
        addHeading("backpack", Component.literal("获取和储存"));
        addParagraph(Component.literal("魔力一般可以在魔力属性对应的物理变化中收集. 如果使用特定的结构(产能机器), 集中这些变化, 可以更高效的收集魔力."));
        addParagraph(Component.literal("魔力需要依附魔导进行存储. 收集魔导材料来制作魔力储存容器(如魔力单元)."));
        addParagraph(Component.literal("常见的魔力储存容器: 魔力单元, 魔力缓存器等"));
    }
}
