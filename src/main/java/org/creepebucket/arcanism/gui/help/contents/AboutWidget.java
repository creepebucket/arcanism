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
        super(Component.literal("关于 Arcanism"));
    }

    @Override
    public void buildContent() {
        acceptSegment(new TextureWidget(Coordinate.fromCenterTop(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/ui/logo_long.png"), Coordinate.fromTopLeft(160, 32)).centerAlign());
        addParagraph(Component.literal("Arcanism 是一个融合了科技要素和编程要素的魔法 Mod。这个 Mod 以魔力的生产与消耗作为核心玩法，构建了一套魔力体系。"));
        addParagraph(Component.literal("Mod版本 " + ModList.get().getModContainerById(MODID).orElseThrow().getModInfo().getVersion().toString()));
        addHeading("people", Component.literal("主要贡献者"));
        addParagraph(Component.literal("程序: creepebucket"));
        addParagraph(Component.literal("美术: LettyWhiterock_"));
    }
}
