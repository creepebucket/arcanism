package org.creepebucket.arcanism.gui.help;

import net.minecraft.ChatFormatting;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.lib.api.*;
import org.creepebucket.arcanism.gui.lib.api.widgets.Clickable;
import org.creepebucket.arcanism.gui.lib.api.widgets.Lifecycle;
import org.creepebucket.arcanism.gui.lib.widgets.*;
import org.creepebucket.arcanism.utils.ModColors;
import org.creepebucket.arcanism.utils.ModUtils;

import java.util.*;
import java.util.function.Consumer;

import static org.creepebucket.arcanism.Arcanism.MODID;
import static org.creepebucket.arcanism.gui.lib.api.Coordinate.*;

public class HelpWindowWidgets {
    public static class SidebarWidget extends Widget implements Lifecycle {
        public ModUtils.RecursiveMap<String> directory;
        public Consumer<String> onSelectionChange;
        public String selectedName;
        public Widget div, bg;
        public ScrollbarWidget scrollbar;
        public ScrollRegionWidget scroll;
        public int topPadding = 4;

        public SidebarWidget(Coordinate pos, Coordinate size, ModUtils.RecursiveMap<String> directory) {
            super(pos, size);

            this.directory = directory;
            this.onSelectionChange = name -> {};
        }

        public void recalculateLayout() {
            List<BarItem> visible = new ArrayList<>();

            for (Widget child : div.children) {
                if (child instanceof BarItem item) visible.add(item);
            }

            for (int i = 0; i < visible.size(); i++) {
                BarItem item = visible.get(i);
                if (!item.expanded) continue;
                visible.addAll(i + 1, item.subItems.values());
            }

            for (int i = 0; i < visible.size(); i++) {
                BarItem item = visible.get(i);
                int parentIndex = item.parent instanceof BarItem parent ? visible.indexOf(parent) : -1;
                int parentTop = parentIndex < 0 ? 0 : parentIndex * BarItem.rowHeight + topPadding;
                int target = i * BarItem.rowHeight + topPadding - parentTop - item.originalY();
                item.dy.set(target + 0.5);
            }

            int contentHeight = visible.size() * BarItem.rowHeight;
            bg.dw.setImmediate(div.w());
            bg.dh.set(contentHeight + 4);

            int maxScroll = Math.max(0, contentHeight - h());
            div.dy.set(Math.max(-maxScroll, Math.min(0, div.dy.target)));
            scrollbar.region = fromTopLeft(-maxScroll, 0);
            scrollbar.contentLength = contentHeight;
            scroll.region = fromTopLeft(-maxScroll, 0);

            if (maxScroll > 0) {
                scroll.enable();
                scrollbar.animations.removeIf(animation -> animation instanceof Animation.FadeOut);
                if (!scrollbar.enabled) {
                    scrollbar.enable();
                    scrollbar.addAnimation(new Animation.FadeIn(0.3), 0);
                }
            } else {
                scroll.disable();
                if (scrollbar.enabled && scrollbar.animations.stream().noneMatch(animation -> animation instanceof Animation.FadeOut))
                    scrollbar.addAnimation(new Animation.FadeOut(0.3).noDeletion(), 0);
                div.dy.set(0);
            }
        }

        public void refreshSelection() {
            for (Widget child : div.children) {
                if (child instanceof BarItem item) item.refreshSelection();
            }
        }

        @Override
        public void onInitialize() {
            var cull = addChild(new CullAreaWidget(ZERO, FULL));
            div = cull.addChild(new BlankWidget(ZERO, FULL));
            bg = div.addChild(new RectangleWidget(ZERO, ZERO).applyTheme(ThemeTemplate.DARK_BG));

            for (String k : directory.keySet()) {
                div.addChild(new BarItem(fromTopLeft(5, topPadding), fromTopRight(-5, BarItem.rowHeight), k, this, directory.get(k)));
            }

            scrollbar = (ScrollbarWidget) addChild(new ScrollbarWidget(fromTopRight(-4, 0), fromBottomLeft(4, 0), ZERO, div.dy, "y").reverseDirection().bgColor(0));
            scrollbar.disable();
            scroll = (ScrollRegionWidget) addChild(new ScrollRegionWidget(ZERO, FULL, ZERO, BarItem.rowHeight, div.dy));
            scroll.disable();
            recalculateLayout();
        }

        public static class BarItem extends Widget implements Lifecycle, Clickable {
            public String name;
            public SidebarWidget bar;
            public Map<String, BarItem> subItems = new LinkedHashMap<>();
            public ModUtils.RecursiveMap<String> directory;
            public boolean expanded = false;
            public Widget arrow;
            public TextWidget text;
            public static int rowHeight = 16;

            public BarItem(Coordinate pos, Coordinate size, String name, SidebarWidget bar, ModUtils.RecursiveMap<String> directory) {
                super(pos, size);

                this.name = name;
                this.bar = bar;
                this.directory = directory;
            }

            @Override
            public void onInitialize() {
                arrow = addChild(new TextureWidget(fromCenterLeft(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons/small_arrow_" + (directory == null ? "in.png" : "right.png")), fromTopLeft(8, 8)).centerAlignY());
                text = (TextWidget) addChild(new TextWidget(fromCenterLeft(10, 0), Component.translatable("gui.arcanism.help.dir." + name)).noShadow().centerAlignY());

                if (directory == null) return;

                // 递归地添加子项
                for (String key : directory.keySet()) {
                    var item = new BarItem(fromTopLeft(10, -3), fromTopRight(-10, rowHeight), key, bar, directory.get(key));
                    subItems.put(key, item);
                    addChild(item);
                    item.disable();
                }
            }

            public void refreshSelection() {
                var label = Component.translatable("gui.arcanism.help.dir." + name);
                text.setText(name.equals(bar.selectedName) ? label.withStyle(ChatFormatting.BOLD) : label);
                dx.set(name.equals(bar.selectedName) ? 5 : 0);

                for (BarItem sub : subItems.values()) sub.refreshSelection();
            }

            @Override
            public boolean mouseClickedChecked(MouseButtonEvent event, boolean fromMouse) {
                if (directory == null) {
                    // 叶子节点触发选择页面
                    if (Objects.equals(name, bar.selectedName)) return true;
                    bar.selectedName = name;
                    bar.refreshSelection();

                    bar.onSelectionChange.accept(name);
                } else if (expanded) {
                    // 收起
                    arrow.removeMyself();
                    expanded = false;
                    arrow = addChild(new TextureWidget(fromCenterLeft(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons/small_arrow_right.png"), fromTopLeft(8, 8)).centerAlignY());

                    for (BarItem i : subItems.values()) {
                        i.animations.removeIf(animation -> animation instanceof Animation.FadeIn);
                        i.addAnimation(new Animation.FadeOut.ToLeft(0.2).noDeletion(), 0);
                        i.dy.set(0);
                    }
                } else {
                    // 展开
                    arrow.removeMyself();
                    expanded = true;
                    arrow = addChild(new TextureWidget(fromCenterLeft(0, 0), Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons/small_arrow_down.png"), fromTopLeft(8, 8)).centerAlignY());

                    for (BarItem i : subItems.values()) {
                        i.enable();
                        i.animations.removeIf(animation -> animation instanceof Animation.FadeOut);
                        i.addAnimation(new Animation.FadeIn.FromLeft(0.2), 0);
                    }
                }

                bar.recalculateLayout();
                return true;
            }
        }
    }

    public static abstract class HelpBodyWidget extends Widget implements Lifecycle {
        public Widget textDiv, bg;
        public String name;
        public int accumulatedY = 10;
        public ScrollbarWidget scrollbar;
        public ScrollRegionWidget scroll;

        public HelpBodyWidget(String name) {
            super(ZERO, FULL);

            this.name = name;
        }

        public void acceptSegment(Widget widget) {
            widget.dy.setImmediate(accumulatedY);
            textDiv.addChild(widget);
            // widget.addAnimation(new Animation.FadeIn.FromTop(0.3), 0);
            accumulatedY += widget.h() + 10;
        }

        /**
         * 添加一段正文, 按内容区宽度自动换行。
         *
         * @param text 正文内容
         */
        public void addParagraph(Component text) {
            acceptSegment(new MultilineTextWidget(fromTopLeft(6, 0), fromTopRight(-14, 0), text).lineHeight(12).noShadow().mainColor(0xffd6d6d6));
        }

        /**
         * 添加一段正文, 按内容区宽度自动换行。
         *
         * @param key 翻译键
         */
        public void addParagraph(String key) {
            addParagraph(Component.translatable(key));
        }

        /**
         * 添加一段正文, 按内容区宽度自动换行。文字原样显示, 不经过翻译。
         *
         * @param text 正文内容
         */
        public void addParagraphLiteral(String text) {
            addParagraph(Component.literal(text));
        }

        /**
         * 添加小节标题: 16x16 图标 + 加粗标题 + 分隔线。
         *
         * @param icon  图标路径, 相对 assets/arcanism/, 如 textures/gui/icons/directory.png
         * @param title 标题文字
         */
        public void addHeading(String icon, Component title) {
            var row = new Widget.BlankWidget(fromTopLeft(0, 0), fromTopRight(0, 20));
            row.addChild(new TextureWidget(fromTopLeft(6, 2), Identifier.fromNamespaceAndPath(MODID, icon), fromTopLeft(16, 16)).mainColor(ModColors.MAIN_COLOR_M));
            row.addChild(new TextWidget(fromTopLeft(28, 5), title.copy().withStyle(ChatFormatting.BOLD)).noShadow().mainColor(0xffffffff));
            row.addChild(new RectangleWidget(fromTopLeft(28, 17), fromTopRight(-40, 1)).mainColor(ModColors.MAIN_COLOR_M.withAlpha(0x50)));
            acceptSegment(row);
        }

        /**
         * 添加小节标题: 16x16 图标 + 加粗标题 + 分隔线。
         *
         * @param icon  图标路径, 相对 assets/arcanism/, 如 textures/gui/icons/directory.png
         * @param titleKey 标题翻译键
         */
        public void addHeading(String icon, String titleKey) {
            addHeading(icon, Component.translatable(titleKey));
        }

        /**
         * 添加小节标题: 16x16 图标 + 加粗标题 + 分隔线。标题原样显示, 不经过翻译。
         *
         * @param icon  图标路径, 相对 assets/arcanism/, 如 textures/gui/icons/directory.png
         * @param title 标题文字
         */
        public void addHeadingLiteral(String icon, String title) {
            addHeading(icon, Component.literal(title));
        }

        /**
         * 添加一条带图标的条目: 8x8 图标 + 标签 + 缩进说明。
         *
         * @param icon  图标路径, 相对 assets/arcanism/, 如 textures/gui/icons/small_arrow_right.png
         * @param label 条目标签
         * @param note  条目说明, 按内容区宽度自动换行
         */
        public void addEntry(String icon, Component label, Component note) {
            var row = new Widget.BlankWidget(fromTopLeft(0, 0), fromTopRight(0, 12));
            row.addChild(new TextureWidget(fromTopLeft(10, 2), Identifier.fromNamespaceAndPath(MODID, icon), fromTopLeft(8, 8)).mainColor(ModColors.MAIN_COLOR_M));
            row.addChild(new TextWidget(fromTopLeft(22, 1), label).noShadow().mainColor(0xffe8feff));
            acceptSegment(row);
            acceptSegment(new MultilineTextWidget(fromTopLeft(22, 0), fromTopRight(-30, 0), note).lineHeight(11).noShadow().mainColor(0xff9a9a9a));
        }

        /**
         * 添加一条带图标的条目: 8x8 图标 + 标签 + 缩进说明。
         *
         * @param icon  图标路径, 相对 assets/arcanism/, 如 textures/gui/icons/small_arrow_right.png
         * @param labelKey 条目标签翻译键
         * @param noteKey  条目说明翻译键, 按内容区宽度自动换行
         */
        public void addEntry(String icon, String labelKey, String noteKey) {
            addEntry(icon, Component.translatable(labelKey), Component.translatable(noteKey));
        }

        /**
         * 添加一条带图标的条目: 8x8 图标 + 标签 + 缩进说明。文字原样显示, 不经过翻译。
         *
         * @param icon  图标路径, 相对 assets/arcanism/, 如 textures/gui/icons/small_arrow_right.png
         * @param label 条目标签
         * @param note  条目说明, 按内容区宽度自动换行
         */
        public void addEntryLiteral(String icon, String label, String note) {
            addEntry(icon, Component.literal(label), Component.literal(note));
        }

        /**
         * 添加提示框, 主题色为动量青, 用于补充说明。
         *
         * @param title 框标题
         * @param text  框内正文
         */
        public void addNote(Component title, Component text) {
            addCallout("textures/gui/icons/information.png", title, ModColors.MAIN_COLOR_M, text);
        }

        /**
         * 添加提示框, 主题色为动量青, 用于补充说明。
         *
         * @param titleKey 框标题翻译键
         * @param textKey  框内正文翻译键
         */
        public void addNote(String titleKey, String textKey) {
            addNote(Component.translatable(titleKey), Component.translatable(textKey));
        }

        /**
         * 添加提示框, 主题色为动量青, 用于补充说明。文字原样显示, 不经过翻译。
         *
         * @param title 框标题
         * @param text  框内正文
         */
        public void addNoteLiteral(String title, String text) {
            addNote(Component.literal(title), Component.literal(text));
        }

        /**
         * 添加重要框, 主题色为温度红, 用于强调不可忽略的内容。
         *
         * @param title 框标题
         * @param text  框内正文
         */
        public void addImportant(Component title, Component text) {
            addCallout("textures/gui/icons/warning.png", title, ModColors.MAIN_COLOR_T, text);
        }

        /**
         * 添加重要框, 主题色为温度红, 用于强调不可忽略的内容。
         *
         * @param titleKey 框标题翻译键
         * @param textKey  框内正文翻译键
         */
        public void addImportant(String titleKey, String textKey) {
            addImportant(Component.translatable(titleKey), Component.translatable(textKey));
        }

        /**
         * 添加重要框, 主题色为温度红, 用于强调不可忽略的内容。文字原样显示, 不经过翻译。
         *
         * @param title 框标题
         * @param text  框内正文
         */
        public void addImportantLiteral(String title, String text) {
            addImportant(Component.literal(title), Component.literal(text));
        }

        /**
         * 添加警告框, 主题色为辐射黄, 用于提醒可能出错的用法。
         *
         * @param title 框标题
         * @param text  框内正文
         */
        public void addWarning(Component title, Component text) {
            addCallout("textures/gui/icons/warning.png", title, ModColors.MAIN_COLOR_R, text);
        }

        /**
         * 添加警告框, 主题色为辐射黄, 用于提醒可能出错的用法。
         *
         * @param titleKey 框标题翻译键
         * @param textKey  框内正文翻译键
         */
        public void addWarning(String titleKey, String textKey) {
            addWarning(Component.translatable(titleKey), Component.translatable(textKey));
        }

        /**
         * 添加警告框, 主题色为辐射黄, 用于提醒可能出错的用法。文字原样显示, 不经过翻译。
         *
         * @param title 框标题
         * @param text  框内正文
         */
        public void addWarningLiteral(String title, String text) {
            addWarning(Component.literal(title), Component.literal(text));
        }

        /**
         * 提示框的通用实现: 淡色背景 + 左侧竖条 + 16x16 图标 + 加粗标题 + 正文。
         *
         * @param icon   图标路径, 相对 assets/arcanism/
         * @param title  框标题
         * @param accent 主题色, 决定背景、竖条、图标与标题的颜色
         * @param text   框内正文, 按内容区宽度自动换行
         */
        public void addCallout(String icon, Component title, Color accent, Component text) {
            var row = new Widget.BlankWidget(fromTopLeft(0, 0), fromTopLeft(w(), 0));
            // 先挂到 textDiv 上, 让正文换行时能取到宽度, 高度随后补回 accumulatedY
            acceptSegment(row);

            row.addChild(new RectangleWidget(fromTopLeft(0, 0), fromBottomRight(0, 0)).mainColor(accent.withAlpha(0x12)));
            row.addChild(new RectangleWidget(fromTopLeft(0, 0), fromBottomLeft(2, 0)).mainColor(accent));
            row.addChild(new TextureWidget(fromTopLeft(8, 8), Identifier.fromNamespaceAndPath(MODID, icon), fromTopLeft(16, 16)).mainColor(accent));
            row.addChild(new TextWidget(fromTopLeft(30, 11), title.copy().withStyle(ChatFormatting.BOLD)).noShadow().mainColor(accent));
            var body = new MultilineTextWidget(fromTopLeft(30, 26), fromTopLeft(w() - 40, 0), text).lineHeight(11).noShadow().mainColor(0xffd6d6d6);
            row.addChild(body);

            var height = 26 + body.h() + 8;
            row.dh.setImmediate(height);
            accumulatedY += height;
        }

        /**
         * 提示框的通用实现: 淡色背景 + 左侧竖条 + 16x16 图标 + 加粗标题 + 正文。
         *
         * @param icon   图标路径, 相对 assets/arcanism/
         * @param titleKey  框标题翻译键
         * @param accent 主题色, 决定背景、竖条、图标与标题的颜色
         * @param textKey   框内正文翻译键, 按内容区宽度自动换行
         */
        public void addCallout(String icon, String titleKey, Color accent, String textKey) {
            addCallout(icon, Component.translatable(titleKey), accent, Component.translatable(textKey));
        }

        /**
         * 提示框的通用实现: 淡色背景 + 左侧竖条 + 16x16 图标 + 加粗标题 + 正文。文字原样显示, 不经过翻译。
         *
         * @param icon   图标路径, 相对 assets/arcanism/
         * @param title  框标题
         * @param accent 主题色, 决定背景、竖条、图标与标题的颜色
         * @param text   框内正文, 按内容区宽度自动换行
         */
        public void addCalloutLiteral(String icon, String title, Color accent, String text) {
            addCallout(icon, Component.literal(title), accent, Component.literal(text));
        }

        public void recalculateLayout() {
            int maxScroll = Math.max(0, accumulatedY - h() + 30);

            textDiv.dy.set(Math.clamp(textDiv.dy.target, -maxScroll, 0));
            bg.dw.setImmediate(textDiv.w());
            bg.dh.setImmediate(accumulatedY);
            scrollbar.region = fromTopLeft(-maxScroll, 0);
            scrollbar.contentLength = accumulatedY;
            scroll.region = fromTopLeft(-maxScroll, 0);

            if (maxScroll > 0) {
                scroll.enable();
                scrollbar.animations.removeIf(animation -> animation instanceof Animation.FadeOut);
                if (!scrollbar.enabled) {
                    scrollbar.enable();
                    scrollbar.addAnimation(new Animation.FadeIn(0.3), 0);
                }
            } else {
                scroll.disable();
                if (scrollbar.enabled && scrollbar.animations.stream().noneMatch(animation -> animation instanceof Animation.FadeOut))
                    scrollbar.addAnimation(new Animation.FadeOut(0.3).noDeletion(), 0);
                textDiv.dy.set(0);
            }
        }

        public void rebuildContent() {
            for (Widget child : List.copyOf(textDiv.children))
                if (child != bg) child.removeMyself();

            accumulatedY = 10;
            buildContent();
            recalculateLayout();
        }

        @Override
        public void onInitialize() {
            var cull = addChild(new CullAreaWidget(fromTopLeft(0, 30), fromBottomRight(0, -30)));
            textDiv = cull.addChild(new BlankWidget(ZERO, FULL));
            bg = textDiv.addChildFirst(new RectangleWidget(ZERO, ZERO).applyTheme(ThemeTemplate.DARK_BG));
            addChildFirst(new RectangleWidget(ZERO, fromTopRight(0, 30)).applyTheme(ThemeTemplate.DARK_BG));
            addChild(new TextWidget(fromTopLeft(6, 6), Component.translatable(name)).noShadow().scaled(2));
            addChild(new RectangleWidget(fromTopLeft(6, 26), fromTopRight(-16, 1)));

            scrollbar = (ScrollbarWidget) addChild(new ScrollbarWidget(fromTopRight(-4, 30), fromBottomLeft(4, -30), ZERO, textDiv.dy, "y").reverseDirection().bgColor(0));
            scroll = (ScrollRegionWidget) addChild(new ScrollRegionWidget(ZERO, FULL, ZERO, 32, textDiv.dy));

            rebuildContent();
        }

        @Override
        public void onResize(int width, int height) {
            rebuildContent();
        }

        public abstract void buildContent();
    }
}
