package club.lbplus.cores.value;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.function.Supplier;

@Getter
public class ValueGroup {
    private final String name;
    private final Supplier<Boolean> displayable;
    private final List<Value> values;

    public ValueGroup(String n, Supplier<Boolean> s, Value... values) {
        this.name = n;
        this.displayable = s;
        this.values = Lists.newArrayList(values);
    }

    public ValueGroup(String n, Value... values) {
        this(n, () -> true, values);
    }
}
