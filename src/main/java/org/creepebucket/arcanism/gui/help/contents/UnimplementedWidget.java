package org.creepebucket.arcanism.gui.help.contents;

import net.minecraft.network.chat.Component;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;

public class UnimplementedWidget extends HelpWindowWidgets.HelpBodyWidget {
    public UnimplementedWidget() {
        super(Component.translatable("gui.arcanism.help.page.unimplemented.title"));
    }

    @Override
    public void buildContent() {
        addWarning(Component.translatable("gui.arcanism.help.page.unimplemented.warning"), Component.translatable("gui.arcanism.help.page.unimplemented.warning_text"));
    }
}
