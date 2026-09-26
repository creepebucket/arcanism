package org.creepebucket.arcanism.gui.lib.api;

import java.util.ArrayList;
import java.util.function.Supplier;

public class DynamicValue<T> {
    private final DataManager manager;
    private final String key;

    DynamicValue(DataManager manager, String key) {
        this.manager = manager;
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) manager.values.get(key);
    }

    public void set(T value) {
        manager.update(key, value);
    }

	public DynamicValue<T> whenFirstDataArrivesDo(Runnable hook) {
		if (manager != null) manager.onFirstArrival.put(key, hook);
		return this;
	}

	public DynamicValue<T> whenDataChangedDo(Runnable hook) {
		if (manager != null) manager.onChange.computeIfAbsent(key, k -> new ArrayList<>()).add(hook);
		return this;
	}

    public LocalValue<T> copyToLocal() {
        return DynamicValue.localValue(get());
    }

    /**
     * 从函数里拆, 适用于DynamicValue<Map<?, ?>> 拆键的情况
     */
    public static <T> DynamicValue<T> fromSupplier(Supplier<T> supplier) {
        return new DynamicValue<T>(null, null) {
            @Override
            public T get() {
                return supplier.get();
            }

            @Override
            public void set(T value) {
            }
        };
    }

    public static <T> LocalValue<T> staticValue(T value) {
        return new LocalValue<>(value, true);
    }
    public static <T> LocalValue<T> localValue(T value) {
        return new LocalValue<>(value, false);
    }

    /**
     * 本地值, 适用于一些特殊情况
     */
    public static class LocalValue<T> extends DynamicValue<T> {
        boolean isStatic;
        T value;

        public LocalValue(T value, boolean isStatic) {
            super(null, null);
            this.value = value;
            this.isStatic = isStatic;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public void set(T value) {
            this.value = isStatic ? this.value : value;
        }
    }
}
