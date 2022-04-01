package club.lbplus.impls.values;

import club.lbplus.cores.value.Value;

import java.util.function.Supplier;

public class BoolValue extends Value<Boolean> {

    public BoolValue(String name, boolean def, Supplier<Boolean> dis) {
        super(name, def, dis);
    }
    public BoolValue(String name, boolean def) {
        super(name, def, () -> true);
    }

}
