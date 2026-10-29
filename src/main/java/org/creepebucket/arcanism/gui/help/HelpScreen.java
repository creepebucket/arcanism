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
		dirMap.put("关于 Arcanism", null);
		dirMap.put("魔力基础", new ModUtils.RecursiveMap<>());
		dirMap.get("魔力基础").put("认识魔力", null);
		dirMap.get("魔力基础").put("产生与转化", null);
		dirMap.put("施法", new ModUtils.RecursiveMap<>());
		dirMap.get("施法").put("魔杖", null);
		dirMap.get("施法").put("魔杖插件", null);
		dirMap.get("施法").put("GUI", new ModUtils.RecursiveMap<>());
		dirMap.get("施法").get("GUI").put("法术面板", null);
		dirMap.get("施法").get("GUI").put("法术编辑", null);
		dirMap.get("施法").get("GUI").put("发射面板", null);
		dirMap.get("施法").put("调试器", new ModUtils.RecursiveMap<>());
		dirMap.get("施法").get("调试器").put("断点", null);
		dirMap.get("施法").get("调试器").put("执行显示", null);
		dirMap.get("施法").get("调试器").put("法术控制", null);
		dirMap.get("施法").put("法术语法", new ModUtils.RecursiveMap<>());
		dirMap.get("施法").get("法术语法").put("语法总览", null);
		dirMap.get("施法").get("法术语法").put("法术tooltip", null);
		dirMap.put("魔导工业", new ModUtils.RecursiveMap<>());
		dirMap.get("魔导工业").put("机器", new ModUtils.RecursiveMap<>());
		dirMap.get("魔导工业").get("机器").put("机器总览", null);
		dirMap.get("魔导工业").get("机器").put("输入/输出", null);
		dirMap.get("魔导工业").get("机器").put("GUI", new ModUtils.RecursiveMap<>());
		dirMap.get("魔导工业").get("机器").get("GUI").put("网络信息", null);
		dirMap.get("魔导工业").get("机器").get("GUI").put("机器信息", null);
		dirMap.get("魔导工业").get("机器").get("GUI").put("超频", null);
		dirMap.get("魔导工业").put("魔力网络", new ModUtils.RecursiveMap<>());
		dirMap.get("魔导工业").get("魔力网络").put("网络总览", null);
		dirMap.get("魔导工业").get("魔力网络").put("节点类型", null);
		dirMap.get("魔导工业").get("魔力网络").put("魔力计算", null);
		dirMap.put("配置文件", new ModUtils.RecursiveMap<>());
		dirMap.get("配置文件").put("玩法", null);
		dirMap.get("配置文件").put("视觉效果", null);
		dirMap.get("配置文件").put("网络", null);


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
