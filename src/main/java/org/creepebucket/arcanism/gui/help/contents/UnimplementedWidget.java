package org.creepebucket.arcanism.gui.help.contents;

import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class UnimplementedWidget extends HelpWindowWidgets.HelpBodyWidget {
    public UnimplementedWidget() {
        super("gui.arcanism.help.page.unimplemented.title");
    }

    @Override
    public void buildContent() {
        addWarning("gui.arcanism.help.page.unimplemented.warning", "gui.arcanism.help.page.unimplemented.warning_text");
    }
}
