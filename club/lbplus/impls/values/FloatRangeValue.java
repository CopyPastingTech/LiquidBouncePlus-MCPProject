package club.lbplus.impls.values;

import club.lbplus.cores.value.Value;
import club.lbplus.utils.math.RandomUtils;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class FloatRangeValue extends Value<Float> {

    private final float minimum;
    private final float maximum;
    private float minValue;
    private float maxValue;
    private final float increment;

    public FloatRangeValue(String n, float defMin, float defMax, float min, float max, float incre, Supplier<Boolean> display) {
        super(n, defMin, display);

        this.minimum = min;
        this.maximum = max;
        this.increment = incre;
        this.minValue = defMin;
        this.maxValue = defMax;
    }

    public FloatRangeValue(String n, float defMin, float defMax, float min, float max, float incre) {
        this(n, defMin, defMax, min, max, incre, () -> true);
    }

    public void set(float min, float max) {
        if (min > max)
            min = max;

        if (max < min)
            max = min;

        this.minValue = min;
        this.maxValue = max;
    }

    @Override
    public Float get() {
        return RandomUtils.randomRange(minValue, maxValue);
    }

}