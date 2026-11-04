package org.creepebucket.arcanism.gui.help.contents.config;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class VisualsWidget extends HelpWindowWidgets.HelpBodyWidget {
    public VisualsWidget() {
        super(Component.literal("视觉效果"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.literal("视觉相关的配置都位于 config/arcanism-common.toml 的 [visual] 段中, 修改后需要重启游戏或服务器才会生效."));
        addHeading("description", Component.literal("动画"));
        addEntry("1_small", Component.literal("disable_animations"), Component.literal("禁用动画, 默认关闭. 开启后页面切换等界面过渡动画会被直接跳过, 适合低配置设备或不喜欢动画的玩家."));
        addNote(Component.literal("该选项只影响客户端的界面动画表现, 不会改变任何实际玩法数值."));
    }
}
