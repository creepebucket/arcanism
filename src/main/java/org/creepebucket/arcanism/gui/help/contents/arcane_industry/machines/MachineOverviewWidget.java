package org.creepebucket.arcanism.gui.help.contents.arcane_industry.machines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class MachineOverviewWidget extends HelpWindowWidgets.HelpBodyWidget {
    public MachineOverviewWidget() {
        super(Component.translatable("gui.arcanism.help.page.machine_overview.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.machine_overview.intro"));
        addParagraph(Component.translatable("gui.arcanism.help.page.machine_overview.paragraph.placement"));
        addParagraph(Component.translatable("gui.arcanism.help.page.machine_overview.paragraph.filling"));
        addHeading("debugger_spell", Component.translatable("gui.arcanism.help.page.machine_overview.heading.gui"));
        addParagraph(Component.translatable("gui.arcanism.help.page.machine_overview.paragraph.gui_interaction"));
        addParagraph(Component.translatable("gui.arcanism.help.page.machine_overview.paragraph.gui_layout"));
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/examples/machine1.png"), Coordinate.fromTopLeft(128, 80)).centerAlign());
        addEntry("1_small", Component.translatable("gui.arcanism.help.page.machine_overview.entry.title_bar.label"), Component.translatable("gui.arcanism.help.page.machine_overview.entry.title_bar.note"));
        addEntry("2_small", Component.translatable("gui.arcanism.help.page.machine_overview.entry.close_button.label"), Component.translatable("gui.arcanism.help.page.machine_overview.entry.close_button.note"));
        addEntry("3_small", Component.translatable("gui.arcanism.help.page.machine_overview.entry.resize_handle.label"), Component.translatable("gui.arcanism.help.page.machine_overview.entry.resize_handle.note"));
        addParagraph(Component.translatable("gui.arcanism.help.page.machine_overview.paragraph.gui_resize"));
        addParagraph(Component.translatable("gui.arcanism.help.page.machine_overview.paragraph.window_manager"));
    }
}
