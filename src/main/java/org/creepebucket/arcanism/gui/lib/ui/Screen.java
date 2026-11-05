package org.creepebucket.arcanism.gui.lib.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.world.entity.player.Inventory;
import org.creepebucket.arcanism.client.ClientUiContext;
import org.creepebucket.arcanism.gui.lib.api.SlotManipulationScreen;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.network.dataPackets.HookTriggerPacket;
import org.creepebucket.arcanism.network.dataPackets.SimpleKvPacket;
import org.lwjgl.glfw.GLFW;

import static org.creepebucket.arcanism.gui.lib.api.Coordinate.updateContext;

public abstract class Screen<M extends Menu> extends SlotManipulationScreen<M> implements WidgetManager {

    public Widget.Root root = new Widget.Root();

    public Screen(M menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    public abstract void buildWidget();

    @Override
    public Widget.Root getRoot() {
        return root;
    }

    @Override
    public void setRoot(Widget.Root root) {
        this.root = root;
    }

    @Override
    public Screen<? extends Menu> getScreen() {
        return this;
    }

    @Override
    public void init() {
        this.resetWidgets();

        this.imageWidth = this.width;
        this.imageHeight = this.height;
        super.init();

        // 1. 把屏幕位置同步给 Menu
        this.menu.guiLeft = this.leftPos;
        this.menu.guiTop = this.topPos;
        updateContext(this.width, this.height, this.leftPos, this.topPos);
        ClientUiContext.setFont(this.font);

        // 2. 绑定发包
        this.menu.dataManager.bindServerSender((key, value) -> {
            var packet = new ServerboundCustomPayloadPacket(new SimpleKvPacket(key, value));
            Minecraft.getInstance().getConnection().send(packet);
        });
        this.menu.dataManager.flushPullRequests();

        this.menu.hooks.bindServerSender((hookId, args) -> {
            var packet = new ServerboundCustomPayloadPacket(new HookTriggerPacket(hookId, args));
            Minecraft.getInstance().getConnection().send(packet);
        });

        // 3. 关键点：调用 Menu 的 reportScreenSize
        // 这会触发 Menu 里所有控件的 onInitialize()，让它们根据新的 guiLeft/Top 计算位置
        this.menu.reportScreenSize(this.width, this.height);

        buildWidget();
    }

    @Override
    public void resize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        super.resize(width, height);

        // 窗口大小变了，更新 Menu 变量并通知控件重算位置
        this.menu.guiLeft = this.leftPos;
        this.menu.guiTop = this.topPos;
        updateContext(width, height, this.leftPos, this.topPos);
        ClientUiContext.setFont(this.font);
        this.menu.reportScreenSize(width, height);

        this.resizeWidgets(width, height);
    }

    @Override
    public void removed() {
        this.destroyWidgets();
        super.removed();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.tickWidgets();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        graphics.nextStratum();

        this.renderWidgets(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        this.renderWidgetLabels(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean fromMouse) {
        super.mouseClicked(event, fromMouse);
        return this.widgetMouseClicked(event, fromMouse);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        super.mouseReleased(event);
        return this.widgetMouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        super.mouseDragged(event, dragX, dragY);
        return this.widgetMouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        return this.widgetMouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.hasAltDown() && event.input() == GLFW.GLFW_KEY_S) {
            Minecraft.getInstance().player.connection.sendCommand("arcanism help");
            return true;
        }

        super.keyPressed(event);
        return this.widgetKeyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        super.keyReleased(event);
        return this.widgetKeyReleased(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        super.charTyped(event);
        return this.widgetCharTyped(event);
    }
}
