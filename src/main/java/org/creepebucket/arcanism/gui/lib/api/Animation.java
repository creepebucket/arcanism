package org.creepebucket.arcanism.gui.lib.api;

import org.creepebucket.arcanism.ModConfig;

import static org.creepebucket.arcanism.utils.ModUtils.now;

public abstract class Animation {
    public double start, duration = 0; // 秒
    public double dx = 0, dy = 0, dw = 0, dh = 0;
    public double alphaMultMain = 1, alphaMultBg = 1, alphaMultText = 1;
    public boolean started = false;

    public boolean isActive() {
        var now = now();
        return now >= start;
    }

    public boolean isExpired() {
        return ModConfig.CONFIG.disableAnimations.get() || start + duration <= now();
    }
    public boolean isStarted() {
        return now() >= start && !started;
    }

    public abstract void step(double dt);

    public void onExpire(Widget widget) {
    }

    public void onStart(Widget widget) {
        started = true;
    }

    public static class FadeIn extends Animation {
        public FadeIn(double duration) {
            this.duration = duration;
        }

        @Override
        public void onStart(Widget widget) {
            super.onStart(widget);
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public void step(double dt) {
            var mult = (now() - start) / duration;
            alphaMultMain = mult;
            alphaMultBg = mult;
            alphaMultText = mult;
        }

        public static class FromLeft extends FadeIn {
            public FromLeft(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dx = -Math.pow(start + duration - now(), 3) * 1000;
            }
        }

        public static class FromRight extends FadeIn {
            public FromRight(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dx = Math.pow(start + duration - now(), 3) * 1000;
            }
        }

        public static class FromTop extends FadeIn {
            public FromTop(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dy = -Math.pow(start + duration - now(), 3) * 1000;
            }
        }

        public static class FromBottom extends FadeIn {
            public FromBottom(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dy = Math.pow(start + duration - now(), 3) * 1000;
            }
        }

        public static class Static extends FadeIn {
            public Static(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
            }
        }
    }

    public static class FadeOut extends Animation {
        public boolean doDelete = true;

        public FadeOut(double duration) {
            this.duration = duration;
        }

        @Override
        public void step(double dt) {
            var mult = 1 - (now() - start) / duration;
            alphaMultMain = mult;
            alphaMultBg = mult;
            alphaMultText = mult;
        }

        @Override
        public void onExpire(Widget widget) {
            if (doDelete) widget.removeMyself();
            else widget.disable();
        }

        public FadeOut noDeletion() {
            doDelete = false;
            return this;
        }

        public static class ToLeft extends FadeOut {
            public ToLeft(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dx = -Math.pow(now() - start, 3) * 1000;
            }
        }

        public static class ToRight extends FadeOut {
            public ToRight(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dx = Math.pow(now() - start, 3) * 1000;
            }
        }

        public static class ToTop extends FadeOut {
            public ToTop(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dy = -Math.pow(now() - start, 3) * 1000;
            }
        }

        public static class ToBottom extends FadeOut {
            public ToBottom(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
                super.step(dt);
                dy = Math.pow(now() - start, 3) * 1000;
            }
        }

        public static class Static extends FadeOut {
            public Static(double duration) {
                super(duration);
            }

            @Override
            public void step(double dt) {
            }
        }
    }

    public static class Transparent extends Animation {
        public Transparent(double duration) {
            this.duration = duration;
        }

        @Override
        public void step(double dt) {
            alphaMultMain = 0;
            alphaMultBg = 0;
            alphaMultText = 0;
        }
    }

    public static class TransitionColor extends Animation {
        public enum Target {
            MAIN,
            BG,
            TEXT
        }

        public Target target;
        public Color color;
        public double fromHue, fromSaturation, fromValue, fromAlpha, toHue, toSaturation, toValue, toAlpha;

        public TransitionColor(double duration, int newColor, Target target) {
            this(duration, new Color(newColor), target);
        }

        public TransitionColor(double duration, Color newColor, Target target) {
            this.duration = duration;
            this.target = target;

            var to = toHsv(newColor);
            toHue = to[0];
            toSaturation = to[1];
            toValue = to[2];
            toAlpha = newColor.a;
        }

        @Override
        public void onStart(Widget widget) {
            super.onStart(widget);

            while (color == null) {
                color = switch (target) {
                    case MAIN -> widget.originalMainColor;
                    case BG -> widget.originalBgColor;
                    case TEXT -> widget.originalTextColor;
                };
                if (color == null) widget = widget.parent;
            }

            var from = toHsv(color);
            fromSaturation = from[1];
            fromHue = fromSaturation == 0 ? toHue : from[0];
            fromValue = from[2];
            fromAlpha = color.a;

            if (toSaturation == 0) toHue = fromHue;
        }

        public static double[] toHsv(Color color) {
            var r = color.r / 255.0;
            var g = color.g / 255.0;
            var b = color.b / 255.0;
            var max = Math.max(r, Math.max(g, b));
            var min = Math.min(r, Math.min(g, b));
            var delta = max - min;

            if (delta == 0) return new double[]{0, 0, max};

            var hue = max == r ? (g - b) / delta : max == g ? (b - r) / delta + 2 : (r - g) / delta + 4;
            if (hue < 0) hue += 6;
            return new double[]{hue / 6, delta / max, max};
        }

        @Override
        public void step(double dt) {
            if (color == null) return;

            var ratio = isExpired() ? 1 : Math.max(0, Math.min(1, (now() - start) / duration));

            // 色相沿最短路径渐变
            var hueDelta = toHue - fromHue;
            if (hueDelta > 0.5) hueDelta -= 1;
            else if (hueDelta < -0.5) hueDelta += 1;

            var hue = (fromHue + hueDelta * ratio + 1) % 1;
            var saturation = fromSaturation + (toSaturation - fromSaturation) * ratio;
            var value = fromValue + (toValue - fromValue) * ratio;

            var i = (int) (hue * 6);
            var f = hue * 6 - i;
            var p = value * (1 - saturation);
            var q = value * (1 - f * saturation);
            var t = value * (1 - (1 - f) * saturation);

            color.r = (int) Math.round(switch (i % 6) {
                case 0 -> value;
                case 1 -> q;
                case 2 -> p;
                case 3 -> p;
                case 4 -> t;
                default -> value;
            } * 255);
            color.g = (int) Math.round(switch (i % 6) {
                case 0 -> t;
                case 1 -> value;
                case 2 -> value;
                case 3 -> q;
                case 4 -> p;
                default -> p;
            } * 255);
            color.b = (int) Math.round(switch (i % 6) {
                case 0 -> p;
                case 1 -> p;
                case 2 -> t;
                case 3 -> value;
                case 4 -> value;
                default -> q;
            } * 255);
            color.a = (int) Math.round(fromAlpha + (toAlpha - fromAlpha) * ratio);
        }
    }
}
