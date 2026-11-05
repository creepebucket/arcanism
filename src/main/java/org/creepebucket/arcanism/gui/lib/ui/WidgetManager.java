package org.creepebucket.arcanism.gui.lib.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.creepebucket.arcanism.client.ClientUiContext;
import org.creepebucket.arcanism.gui.lib.api.ClientSlotManager;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.gui.lib.api.widgets.*;

import java.util.List;

import static org.creepebucket.arcanism.utils.ModUtils.now;

public interface WidgetManager {

    Widget.Root getRoot();

    void setRoot(Widget.Root root);

    Screen<? extends Menu> getScreen();

    default double dt() {
        return this.getRoot().dt;
    }

    default double lastFrame() {
        return this.getRoot().lastFrame;
    }

    default float lastPartialTick() {
        return this.getRoot().lastPartialTick;
    }

    default void resetWidgets() {
        for (Widget widget : this.getRoot().allChild()) {
            if (widget instanceof Lifecycle lifecycle) {
                lifecycle.onDestroy();
            }
        }

        Widget.Root root = new Widget.Root();
        root.screen = this.getScreen();
        this.setRoot(root);
    }

    default void resizeWidgets(int width, int height) {
        // 清掉旧尺寸缓存后再通知控件重排，保证 onResize 里读到的是新尺寸
        var widgets = List.copyOf(this.getRoot().allChild());
        for (Widget widget : widgets) widget.clearCache();
        for (Widget widget : widgets) {
            if (widget instanceof Lifecycle lifecycle) lifecycle.onResize(width, height);
        }
    }

    default void destroyWidgets() {
        // 界面关闭时，通知 Menu 里的控件被移除了
        for (Widget widget : this.getRoot().allChild()) {
            if (widget instanceof Lifecycle lifecycle) {
                lifecycle.onDestroy();
            }
        }
    }

    default void tickWidgets() {
        // 遍历 menu.widgets 进行逻辑更新
        for (Widget widget : this.getRoot().allChild()) {
            if (widget instanceof Tickable tickable) {
                tickable.tick();
            }
        }
    }

    default void renderWidgets(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Widget.Root root = this.getRoot();
        root.dt = now() - this.lastFrame();
        root.lastFrame = now();
        root.lastPartialTick = partialTick;

        root.renderWidget(graphics, mouseX, mouseY, partialTick, this.dt(), true);

        for (Widget widget : this.getRoot().allChild()) {
            if (widget instanceof Tooltipable tooltipable) {
                if (tooltipable.renderTooltip(graphics, mouseX, mouseY)) return;
            }
            // widget自己的简易tooltip

            if (widget.doShowTooltip && widget.isInBounds(mouseX, mouseY) && widget.isEffectivelyEnabled())
                graphics.setTooltipForNextFrame(ClientUiContext.getFont(), List.of(widget.tooltip.getVisualOrderText()), mouseX, mouseY);
        }
    }

    default void renderWidgetLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        ClientSlotManager.clearAll();
        this.getRoot().renderWidget(graphics, mouseX, mouseY, 0, this.dt(), false);
    }

    // --- 下面全是把输入事件转发给 menu.widgets ---

    default boolean widgetMouseClicked(MouseButtonEvent event, boolean fromMouse) {
        for (Widget widget : this.getRoot().allChild()) {
            if (!widget.isEffectivelyEnabled()) continue;

            if (widget instanceof Clickable clickable) {
                if (clickable.mouseClicked(event, fromMouse)) return true;

                if (widget.isInBounds(event.x(), event.y()) && clickable.mouseClickedChecked(event, fromMouse))
                    return true;
            }
            if (!widget.clickBehaviors.isEmpty() && widget.isInBounds(event.x(), event.y())) {
                for (Runnable behavior : widget.clickBehaviors) behavior.run();
                return true;
            }
        }
        return true;
    }

    default boolean widgetMouseReleased(MouseButtonEvent event) {
        for (Widget widget : this.getRoot().allChild()) {
            if (!widget.isEffectivelyEnabled()) continue;

            if (widget instanceof Clickable clickable) {
                if (clickable.mouseReleased(event)) return true;

                if (widget.isInBounds(event.x(), event.y()) && clickable.mouseReleasedChecked(event)) return true;
            }
        }
        return true;
    }

    default boolean widgetMouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        for (Widget widget : this.getRoot().allChild()) {
            if (!widget.isEffectivelyEnabled()) continue;

            if (widget instanceof MouseDraggable draggable) {
                if (draggable.mouseDragged(event, dragX, dragY)) return true;
            }
        }
        return true;
    }

    default boolean widgetMouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        for (Widget widget : this.getRoot().allChild()) {
            if (!widget.isEffectivelyEnabled()) continue;

            if (widget instanceof MouseScrollable scrollable) {
                if (scrollable.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) return true;
            }
        }
        return true;
    }

    default boolean widgetKeyPressed(KeyEvent event) {
        for (Widget widget : this.getRoot().allChild()) {
            if (!widget.isEffectivelyEnabled()) continue;

            if (widget instanceof KeyInputable inputable) {
                if (inputable.keyPressed(event)) return true;
            }
        }
        return true;
    }

    default boolean widgetKeyReleased(KeyEvent event) {
        for (Widget widget : this.getRoot().allChild()) {
            if (!widget.isEffectivelyEnabled()) continue;

            if (widget instanceof KeyInputable inputable) {
                if (inputable.keyReleased(event)) return true;
            }
        }
        return true;
    }

    default boolean widgetCharTyped(CharacterEvent event) {
        for (Widget widget : this.getRoot().allChild()) {
            if (!widget.isEffectivelyEnabled()) continue;

            if (widget instanceof KeyInputable inputable) {
                if (inputable.charTyped(event)) return true;
            }
        }
        return true;
    }

    default Widget addWidget(Widget widget) {
        widget.screen = this.getScreen();
        for (Widget w : widget.allChild()) w.screen = this.getScreen();
        this.getRoot().addChild(widget);
        return widget;
    }
}
