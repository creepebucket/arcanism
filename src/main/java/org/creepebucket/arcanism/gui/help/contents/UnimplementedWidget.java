package org.creepebucket.arcanism.gui.help.contents;

import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class UnimplementedWidget extends HelpWindowWidgets.HelpBodyWidget {
    public UnimplementedWidget() {
        super("404 Not Found");
    }

    @Override
    public void buildContent() {
        addWarning("目前暂未实现该页面", "正在写, 真的qwq...");
    }
}
