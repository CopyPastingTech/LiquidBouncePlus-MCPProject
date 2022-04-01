package club.lbplus.impls.values;

import club.lbplus.cores.value.Value;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class FloatValue extends Value<Float> {

    private final float minimum;
    private final float maximum;
    private final float increment;

    public FloatValue(String n, float def, float min, float max, float incre, Supplier<Boolean> display) {
        super(n, def, display);

        this.minimum = min;
        this.maximum = max;
        this.increment = incre;
    }

    public FloatValue(String n, float def, float min, float max, float incre) {
        this(n, def, min, max, incre, () -> true);
    }

}
