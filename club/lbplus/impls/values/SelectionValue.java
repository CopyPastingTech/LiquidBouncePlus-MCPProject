package club.lbplus.impls.values;

import com.google.common.collect.Lists;
import club.lbplus.cores.value.Value;
import lombok.Getter;

import java.util.List;
import java.util.function.Supplier;

@Getter
public class SelectionValue extends Value<String> {

    private final List<String> choices;

    public SelectionValue(String n, String def, Supplier<Boolean> d, String... c) {
        super(n, def, d);
        this.choices = Lists.newArrayList(c);
    }

    @Override
    public void set(String newValue) {
        if (value != newValue && choices.contains(value)) {
            final String lastValue = value;
            preChange(lastValue, newValue);
            value = newValue;
            postChange(lastValue, value);
        }
    }

    public SelectionValue(String n, String def, String... c) {
        this(n, def, () -> true, c);
    }

}
