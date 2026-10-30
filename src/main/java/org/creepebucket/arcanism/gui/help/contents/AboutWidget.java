package org.creepebucket.arcanism.gui.help.contents;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.ModList;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;

import static org.creepebucket.arcanism.Arcanism.MODID;

public class AboutWidget extends HelpWindowWidgets.HelpBodyWidget {
    public AboutWidget() {
        super(Component.translatable("gui.arcanism.help.page.about.title"));
    }

    @Override
    public void buildContent() {
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/ui/logo_long.png"), Coordinate.fromTopLeft(160, 32)).centerAlign());
        addParagraph(Component.translatable("gui.arcanism.help.page.about.intro"));
        addParagraph(Component.translatable("gui.arcanism.help.page.about.version", ModList.get().getModContainerById(MODID).orElseThrow().getModInfo().getVersion().toString()));
        addHeading("people", Component.translatable("gui.arcanism.help.page.about.heading.contributors"));
        addParagraph(Component.translatable("gui.arcanism.help.page.about.contributor.programmer"));
        addParagraph(Component.translatable("gui.arcanism.help.page.about.contributor.artist"));
    }
}
