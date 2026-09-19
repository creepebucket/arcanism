package org.creepebucket.arcanism.gui.lib.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.creepebucket.arcanism.client.ClientUiContext;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.api.widgets.Lifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * 多行文本控件：按最低尺寸的宽度自动换行，文本高度超出最低高度时以文本高度为准。
 */
public class MultilineTextWidget extends TextWidget implements Lifecycle {
    /**
     * 自动分割后的文本行
     */
    public List<FormattedCharSequence> lines;
    public int lineHeight = 9;

    public MultilineTextWidget(Coordinate pos, Coordinate size, Component text) {
        super(pos, text);
        originalSize = size;
        setText(text);
    }

    public MultilineTextWidget lineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    @Override
    public MultilineTextWidget noShadow() {
        super.noShadow();
        return this;
    }

    @Override
    public MultilineTextWidget scaled(double fact) {
        super.scaled(fact);
        return this;
    }

    @Override
    public MultilineTextWidget setText(Component text) {
        this.text = text;
        updateLayout();
        return this;
    }

    @Override
    public void onInitialize() {
        updateLayout();
    }

    public void updateLayout() {
        Font font = ClientUiContext.getFont();
        int refWidth = parent == null ? Coordinate.getScreenWidth() : parent.w();
        int refHeight = parent == null ? Coordinate.getScreenHeight() : parent.h();
        int maxWidth = (int) Math.max(1, originalSize.x.apply(refWidth, refHeight) / scale);
        lines = new ArrayList<>();
        FormattedText remaining = text;
        while (!remaining.getString().isEmpty()) {
            FormattedText head = font.substrByWidth(remaining, maxWidth);
            lines.add(Language.getInstance().getVisualOrder(head));
            remaining = tail(remaining, head.getString().length());
        }
        int textHeight = (int) (lines.size() * lineHeight * scale);
        if (textHeight > originalSize.y.apply(refWidth, refHeight))
            originalSize = new Coordinate(originalSize.x, (sw, sh) -> textHeight);
    }

    /**
     * 在字符边界去掉文本头部 skip 个字符，保留剩余部分及其样式。
     */
    public FormattedText tail(FormattedText text, int skip) {
        int[] remaining = {skip};
        List<FormattedText> kept = new ArrayList<>();
        text.visit((style, content) -> {
            int from = Math.min(remaining[0], content.length());
            remaining[0] -= from;
            if (from < content.length())
                kept.add(FormattedText.of(content.substring(from), style));
            return Optional.empty();
        }, Style.EMPTY);
        return FormattedText.composite(kept);
    }

    @Override
    public void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Font font = ClientUiContext.getFont();
        for (int i = 0; i < lines.size(); i++)
            drawScaledString(graphics, font, lines.get(i), menuX(), menuY() + i * lineHeight * (float) scale, (float) scale, mainColorInt(), shadow);
    }
}
