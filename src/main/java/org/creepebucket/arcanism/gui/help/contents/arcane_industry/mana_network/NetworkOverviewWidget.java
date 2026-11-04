package org.creepebucket.arcanism.gui.help.contents.arcane_industry.mana_network;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class NetworkOverviewWidget extends HelpWindowWidgets.HelpBodyWidget {
    public NetworkOverviewWidget() {
        super(Component.literal("网络总览"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("魔力网络是一张由魔力连接器构成的连通图. 每个连接器就是一个网络节点, 机器通过自动连接挂到节点上. 同一个网络里的魔力完全共享, 不同网络之间互不影响."));

        addHeading("compile", Component.literal("连接"));
        addParagraph(Component.literal("右击连接器的一个有效面记录连接起点, 再右击另一个连接器的有效面就完成连接. 同一个面再次连接会先断开旧的对端, 不会出现一条面接两个对象的情况."));
        addParagraph(Component.literal("拆除任意一端都会让这条连接断开. 连接关系保存在方块实体里, 重新加载世界后依然有效."));

        addHeading("directory", Component.literal("合并与分割"));
        addParagraph(Component.literal("两个网络连在一起时, 储量、负载和容量都会相加, 新网络的 id 取两个旧 id 中较小的那个. 因此合并后的网络会继承较小位置节点的身份."));
        addParagraph(Component.literal("断开连接导致网络分裂时, 已有魔力按配置 mana_net_split_mode 分配: half 平分, node_ratio 按两半的节点数量比例, ratio 按两半的缓存容量比例, largest 全部分给节点更多的一半."));

        addHeading("information", Component.literal("查看与调试"));
        addParagraph(Component.literal("输入 /arcanism network info 可以打开网络信息界面, 列出当前维度的所有网络, 支持按 id 搜索, 也可以调整数据刷新间隔."));
        addParagraph(Component.literal("管理员可以用 /arcanism network set、fill、empty 配合 id 或坐标直接修改某个网络的魔力, 用来复现或排查问题."));

        addNote(Component.literal("网络 id 由维度与节点位置共同决定, 取网络内最小的节点位置. 它只是身份标识, 不代表网络的优先级或大小."));
        addImportant(Component.literal("如果一个网络在一整刻内没有被任何机器或连接器访问, 它的数据会被清理掉. 完全断开的孤岛网络不会长期保存其中的魔力."));
    }
}
