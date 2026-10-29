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
            String plain = remaining.getString();
            int fit = Math.max(1, font.substrByWidth(remaining, maxWidth).getString().length());
            int cut = fit;
            // 英文单词不从中间断开: 断点两侧都是英文词字符时继续前移
            while (cut < plain.length() && cut > 0 && plain.charAt(cut - 1) < 0x2E80 && Character.isLetterOrDigit(plain.charAt(cut - 1))
                    && plain.charAt(cut) < 0x2E80 && Character.isLetterOrDigit(plain.charAt(cut))) cut--;
            if (cut == 0) cut = fit;
            // 全角收尾标点不落到下一行行首: 把标点吞回当前行
            while (cut < plain.length() && "，。、；：！？）》」』】”’…～·".indexOf(plain.charAt(cut)) >= 0) cut++;
            lines.add(Language.getInstance().getVisualOrder(slice(remaining, 0, cut)));
            remaining = slice(remaining, cut, plain.length() - cut);
        }
        int textHeight = (int) (lines.size() * lineHeight * scale);
        if (textHeight > originalSize.y.apply(refWidth, refHeight))
            originalSize = new Coordinate(originalSize.x, (sw, sh) -> textHeight);
    }

    /**
     * 在字符边界截取文本, 保留原样式。
     *
     * @param text  源文本
     * @param skip  跳过的开头字符数
     * @param count 保留的字符数, 超出剩余长度时保留全部
     */
    public FormattedText slice(FormattedText text, int skip, int count) {
        int[] range = {skip, count};
        List<FormattedText> kept = new ArrayList<>();
        text.visit((style, content) -> {
            int from = Math.min(range[0], content.length());
            range[0] -= from;
            int take = Math.min(range[1], content.length() - from);
            range[1] -= take;
            if (take > 0) kept.add(FormattedText.of(content.substring(from, from + take), style));
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
