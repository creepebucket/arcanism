package org.creepebucket.arcanism.gui.help.contents.arcane_industry.mana_network;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class NodeTypesWidget extends HelpWindowWidgets.HelpBodyWidget {
    public NodeTypesWidget() {
        super(Component.literal("节点类型"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("目前有三种魔力连接器. 它们的连接规则完全一致, 只是造型和放置方式不同, 按你的布线需要挑一种即可."));

        addEntry("directory", Component.literal("基础魔力连接器"), Component.literal("可以吸附在地面、墙面或天花板上, 体积小, 适合在机器接口附近做转接."));
        addEntry("directory", Component.literal("水平魔力连接器"), Component.literal("落地式, 造型扁平的横向接头, 用来铺水平方向的线路, 可以贴着地面连成一排."));
        addEntry("directory", Component.literal("垂直魔力连接器"), Component.literal("落地式, 带竖直柱身, 用来在竖直方向上穿行, 但可连接的面仍然只有水平方向的四个面."));

        addHeading("warning", Component.literal("可连接面"));
        addParagraph(Component.literal("三种连接器都只接受水平方向的四个面. 点击上下两面会被拒绝, 并提示无法在此面连接."));
        addParagraph(Component.literal("这意味着垂直方向上的连接必须靠连接器自身的造型来实现, 而不是靠点击顶面或底面."));

        addHeading("information", Component.literal("与机器连接"));
        addParagraph(Component.literal("不同机器的魔力接口位置不一样: 太阳能板在四侧, 风力涡轮机在正上方 5 格, 缓存器小型/中型/大型分别在正上方 1/3/4 格, 蒸汽轮机、热交换机、泄压阀、水泵和液体加热器在朝向前方的上方一格."));
        addParagraph(Component.literal("把连接器放在这些位置, 机器每个游戏刻都会自动与它建立连接, 不需要手动连接机器一侧. 连接器之间的连接才需要手动右击."));

        addNote(Component.literal("连接器本身不消耗魔力, 也不提供缓存, 它只负责把机器接进同一张网络. 网络能存多少魔力, 取决于网络里的缓存器和其他机器."));
    }
}
