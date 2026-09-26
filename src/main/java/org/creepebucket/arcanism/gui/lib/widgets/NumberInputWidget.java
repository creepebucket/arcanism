package org.creepebucket.arcanism.gui.lib.widgets;

import org.creepebucket.arcanism.gui.lib.api.Color;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.api.DynamicValue;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.gui.lib.api.widgets.Lifecycle;
import org.creepebucket.arcanism.gui.lib.api.widgets.Tickable;

import static java.lang.Double.parseDouble;
import static net.minecraft.network.chat.Component.literal;
import static org.creepebucket.arcanism.gui.lib.api.Coordinate.fromTopLeft;
import static org.creepebucket.arcanism.gui.lib.api.Coordinate.fromTopRight;
import static org.creepebucket.arcanism.utils.ModUtils.roundDouble;

public class NumberInputWidget extends Widget implements Lifecycle, Tickable {
    public DynamicValue<Double> num;
    public double min, max, step = 1, fact = 10;
    public int depth = 2;
    public boolean showMinMax = true;
    public InputBoxWidget inputBox;

    public NumberInputWidget(Coordinate pos, Coordinate size, DynamicValue<Double> num, double min, double max) {
        super(pos, size);

        this.num = num;
        this.min = min;
        this.max = max;
    }

    @Override
    public void onInitialize() {
        // 最值按钮
        if (showMinMax) {
            addChild(new TextButtonWidget(fromTopLeft(0, 0), fromTopLeft(h(), h()), literal("|<"), () -> num.set(min)));
            addChild(new TextButtonWidget(fromTopRight(0, 0), fromTopLeft(h(), h()), literal(">|"), () -> num.set(max)).rightAlign());
        }

        // 普通按钮
        for (int i = 0; i < depth; i++) {
            int finalI = i;
            addChild(new TextButtonWidget(fromTopLeft((h() + 1) * i + (showMinMax ? h() + 1 : 0), 0), fromTopLeft(h(), h()),
                    literal("<".repeat(depth - i)), () -> {
                num.set(Math.max(min, roundDouble(num.get() - step * Math.pow(fact, depth - finalI - 1))));
            }));
            addChild(new TextButtonWidget(fromTopRight(-(h() + 1) * i - (showMinMax ? h() + 1 : 0), 0), fromTopLeft(h(), h()),
                    literal(">".repeat(depth - i)), () -> {
                num.set(Math.min(max, roundDouble(num.get() + step * Math.pow(fact, depth - finalI - 1))));
            }).rightAlign());
        }

        // 输入框
        inputBox = (InputBoxWidget) addChild(new InputBoxWidget(fromTopLeft((h() + 1) * depth + (showMinMax ? h() + 1 : 0), 0), fromTopRight(-2 * ((h() + 1) * depth + (showMinMax ? h() + 1 : 0)), h()),
                String.valueOf(num.get()), 1024).mainColor(new Color(0)));
    }

    @Override
    public void tick() {
        if (!inputBox.box.isFocused()) {
            inputBox.box.setValue(String.valueOf(num.get()));
            return;
        }
        try {
            var d = parseDouble(inputBox.box.getValue());
            var clamped = Math.clamp(d, min, max);
            if (clamped != num.get()) num.set(clamped);
            if (clamped != d) inputBox.box.setValue(String.valueOf(clamped));
            inputBox.bgColor(bgColor()).tooltip(null);
        } catch (NumberFormatException e) {
            if (!inputBox.box.getValue().isEmpty() && !inputBox.box.getValue().equals("-")) {
                inputBox.box.setValue(String.valueOf(num.get()));
            }
        }
    }

    public NumberInputWidget setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public NumberInputWidget setStep(double step) {
        this.step = step;
        return this;
    }

    public NumberInputWidget setFactor(double fact) {
        this.fact = fact;
        return this;
    }

    public NumberInputWidget disableMinMaxButton() {
        showMinMax = false;
        return this;
    }

    public NumberInputWidget enableMinMaxButton() {
        showMinMax = true;
        return this;
    }

    public void update() {
        children.clear();
        onInitialize();
    }
}
