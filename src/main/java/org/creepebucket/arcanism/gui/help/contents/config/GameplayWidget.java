package org.creepebucket.arcanism.gui.help.contents.config;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class GameplayWidget extends HelpWindowWidgets.HelpBodyWidget {
    public GameplayWidget() {
        super(Component.literal("玩法"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("玩法相关的配置都位于 config/arcanism-common.toml 的 [gameplay] 段中, 修改后需要重启游戏或服务器才会生效."));
        addHeading("temperature", Component.literal("燃料"));
        addEntry("1_small", Component.literal("fuel_value_multplier"), Component.literal("每刻燃烧时间对应的燃料燃值, 默认 8e6 (J/燃烧刻)."));
        addEntry("2_small", Component.literal("more_balanced_fuel"), Component.literal("使燃料热值更贴近现实, 默认关闭. 为了平衡常见燃料的数值削弱, 部分MC特色燃料的数值会被增强."));
        addHeading("compute_mod", Component.literal("法术消耗"));
        addParagraph(Component.literal("法术的魔力消耗由几个参数共同决定, 它们会相乘或相加地影响最终结果."));
        addEntry("1_small", Component.literal("cast_mass"), Component.literal("法术消耗计算使用的固定质量(kg), 默认 70.0."));
        addEntry("2_small", Component.literal("break_energy_per_hardness"), Component.literal("每点方块硬度对应的破坏魔力消耗(J), 默认 1e6."));
        addEntry("3_small", Component.literal("global_cast_cost_mult"), Component.literal("所有法术魔力消耗的全局乘数, 默认 1.0. 调高会让所有法术都更昂贵."));
        addHeading("directory", Component.literal("魔力网络分割"));
        addParagraph(Component.literal("当魔力网络因为拓扑改变(例如拆掉节点或导线)而被分割成两半时, 网络中已有的魔力会按 mana_net_split_mode 指定的方式分配, 默认为 ratio."));
        addEntry("1_small", Component.literal("half"), Component.literal("两半平分已有的魔力."));
        addEntry("2_small", Component.literal("node_ratio"), Component.literal("按两半的节点数量比例分配."));
        addEntry("3_small", Component.literal("ratio"), Component.literal("按两半的缓冲容量比例分配."));
        addEntry("4_small", Component.literal("largest"), Component.literal("全部魔力都分给节点数量较多的一半."));
    }
}
