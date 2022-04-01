package club.lbplus.impls.values;

import club.lbplus.cores.value.Value;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class IntValue extends Value<Integer> {

    private final int minimum;
    private final int maximum;
    private final int increment;

    public IntValue(String n, int def, int min, int max, int incre, Supplier<Boolean> display) {
        super(n, def, display);

        this.minimum = min;
        this.maximum = max;
        this.increment = incre;
    }

    public IntValue(String n, int def, int min, int max, int incre) {
        this(n, def, min, max, incre, () -> true);
    }

}
