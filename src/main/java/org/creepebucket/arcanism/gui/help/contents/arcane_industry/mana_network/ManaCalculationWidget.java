package org.creepebucket.arcanism.gui.help.contents.arcane_industry.mana_network;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class ManaCalculationWidget extends HelpWindowWidgets.HelpBodyWidget {
    public ManaCalculationWidget() {
        super(Component.literal("魔力计算"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("魔力网络每游戏刻(1/20 秒)结算一次. 规则只有三条: 机器上报负载, 机器上报容量, 网络结算储量."));

        addHeading("momentum", Component.literal("负载"));
        addParagraph(Component.literal("机器每个游戏刻把本刻功率换算成负载加到网络上, 产出记为负负载, 消耗记为正负载. 1 W 对应每刻 20 单位魔力."));
        addParagraph(Component.literal("所以网络面板上的功率 W 与内部负载正好相差 20 倍: 面板功率 = 负载 × 20."));

        addHeading("backpack", Component.literal("容量"));
        addParagraph(Component.literal("机器每个游戏刻也会把自身的缓存加进网络, 网络的缓存就是这些值的总和. 每刻结算结束后负载与缓存都会清零, 下一刻意器重新上报."));
        addParagraph(Component.literal("缓存是网络的储量上限. 缓存为零的网络无法存储魔力, 发电机的产出会被直接丢弃."));

        addHeading("radiation", Component.literal("结算"));
        addParagraph(Component.literal("结算时先用上一刻累加的负载扣减储量, 再把结果与缓存逐分量取较小值, 防止储量超过容量."));
        addParagraph(Component.literal("四种魔力分开结算: 辐射、温度、动量、压力各自有独立的储量、负载与容量, 互不换算、互不补足."));

        addHeading("compute_mod", Component.literal("能否运行"));
        addParagraph(Component.literal("消耗器在开始工作前会检查: 当前储量减去本刻负载之后, 四个分量是否都不为负. 只要有一种魔力不够, 这台机器本刻就不工作."));
        addParagraph(Component.literal("因此消耗器不需要额外的开关来判断能源是否足够, 把机器打开并保证网络里有对应的魔力即可."));

        addNote(Component.literal("服务端数据回传间隔由 server_data_sync_interval 控制. 调大可以减少同步开销, 但界面上的数字会更新得更慢, 观察瞬间波动时可以先调小."));
        addWarning(Component.literal("负载与缓存只在网络被访问时才保留. 如果网络的最后一个节点在一整刻内没有被访问, 它的数据会被清理, 重新接通后会从零开始."));
    }
}
