package club.lbplus.cores.value;

import lombok.Getter;

import java.util.function.Supplier;

public class Value<T> {
    @Getter
    protected final String name;
    protected T value;

    protected final Supplier<Boolean> displayable;

    public Value(String name, T defaultValue, Supplier<Boolean> displayable) {
        this.name = name;
        this.value = defaultValue;
        this.displayable = displayable;
    }

    public Supplier<Boolean> isDisplayable() {
        return displayable;
    }

    public T get() {
        return value;
    }

    public void set(T newValue) {
        if (value != newValue) {
            final T lastValue = value;
            preChange(lastValue, newValue);
            value = newValue;
            postChange(lastValue, value);
        }
    }

    public void preChange(T _old, T _new) {}
    public void postChange(T _old, T _new) {}
}
