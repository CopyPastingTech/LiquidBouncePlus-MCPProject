package club.lbplus.impls.values;

import club.lbplus.cores.value.Value;
import club.lbplus.utils.math.RandomUtils;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class IntRangeValue extends Value<Integer> {

    private final int minimum;
    private final int maximum;
    private int minValue;
    private int maxValue;
    private final int increment;

    public IntRangeValue(String n, int defMin, int defMax, int min, int max, int incre, Supplier<Boolean> display) {
        super(n, defMin, display);

        this.minimum = min;
        this.maximum = max;
        this.increment = incre;
        this.minValue = defMin;
        this.maxValue = defMax;
    }

    public IntRangeValue(String n, int defMin, int defMax, int min, int max, int incre) {
        this(n, defMin, defMax, min, max, incre, () -> true);
    }

    public void set(int min, int max) {
        if (min > max)
            min = max;

        if (max < min)
            max = min;

        this.minValue = min;
        this.maxValue = max;
    }

    @Override
    public Integer get() {
        return RandomUtils.randomRange(minValue, maxValue);
    }

}