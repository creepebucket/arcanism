package org.creepebucket.arcanism.gui.help;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.fml.ModList;
import org.creepebucket.arcanism.gui.help.contents.IntroductionWidget;
import org.creepebucket.arcanism.gui.help.contents.UnimplementedWidget;
import org.creepebucket.arcanism.gui.lib.api.Animation;
import org.creepebucket.arcanism.gui.lib.api.ThemeTemplate;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.gui.lib.ui.Screen;
import org.creepebucket.arcanism.gui.lib.widgets.MouseCursorWidget;
import org.creepebucket.arcanism.gui.lib.widgets.RectangleWidget;
import org.creepebucket.arcanism.gui.lib.widgets.TextWidget;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;
import org.creepebucket.arcanism.utils.ModUtils;

import java.util.HashMap;

import static org.creepebucket.arcanism.Arcanism.MODID;
import static org.creepebucket.arcanism.gui.lib.api.Coordinate.*;

public class HelpScreen extends Screen<HelpMenu> {

	public HelpScreen(HelpMenu menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}

	@Override
	public void buildWidget() {
		var div = new Widget.BlankWidget(CENTER, fromBottomLeft(300, -20)).centerAlign().centerAlignY();
		addWidget(div);

		var logo = div.addChild(new RectangleWidget(ZERO, fromTopLeft(100, 45)).applyTheme(ThemeTemplate.DARK_BG));
		logo.addChild(new TextureWidget(fromTopLeft(2, 2), Identifier.fromNamespaceAndPath(MODID, "textures/gui/ui/logo.png"), fromTopLeft(96, 32)).mainColor(-1));
		logo.addChild(new TextWidget(fromBottomLeft(4, -3), Component.literal("帮助菜单")).noShadow().bottomAlignY().mainColor(-1));
		logo.addChild(new TextWidget(fromBottomRight(-4, -3), Component.literal("v" + ModList.get().getModContainerById(MODID).orElseThrow().getModInfo().getVersion().toString())).noShadow().bottomAlignY().rightAlign().mainColor(-1));

		var dirMap = new ModUtils.RecursiveMap<String>();
		dirMap.put("欢迎", null);
		dirMap.put("魔力基础", new ModUtils.RecursiveMap<>());
		dirMap.get("魔力基础").put("魔力", null);

		var contentMap = new HashMap<String, Widget>();
		contentMap.put("欢迎", new IntroductionWidget());

		var sidebar = (HelpWindowWidgets.SidebarWidget) div.addChild(new HelpWindowWidgets.SidebarWidget(fromTopLeft(0, 50), fromBottomLeft(100, -50), dirMap));
		var contentDiv = div.addChild(new Widget.BlankWidget(fromTopLeft(110, 0), fromBottomRight(-110, 0)));

		for (Widget w : contentMap.values()) {
			contentDiv.addChild(w);
			w.disable();
		}

		var unimplemented = contentDiv.addChild(new UnimplementedWidget());
		unimplemented.disable();

		contentMap.get("欢迎").enable();
		contentMap.get("欢迎").addAnimation(new Animation.FadeIn.FromTop(0.3), 0.2);
		sidebar.selectedName = "欢迎";
		sidebar.refreshSelection();

		sidebar.onSelectionChange = (name) -> {
			for (Widget w : contentDiv.children) {
				w.animations.removeIf(animation -> animation instanceof Animation.FadeIn);
				w.addAnimation(new Animation.FadeOut.ToBottom(0.3).noDeletion(), 0);
			}

			var newChild = contentMap.getOrDefault(name, unimplemented);

			newChild.enable();
			newChild.animations.removeIf(animation -> animation instanceof Animation.FadeOut);
			newChild.addAnimation(new Animation.FadeIn.FromTop(0.3), 0.2);
		};

		addWidget(new MouseCursorWidget());
	}
}
