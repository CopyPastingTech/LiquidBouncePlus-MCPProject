package club.lbplus.cores.module;

import club.lbplus.cores.value.Value;
import club.lbplus.LiquidCore;
import club.lbplus.cores.value.ValueGroup;
import club.lbplus.utils.GlobalInstances;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Mod extends GlobalInstances {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final Category category;

    @Getter
    @Setter
    private int keyCode;

    private boolean toggled = false;

    @Getter
    private final List<Value> values;
    @Getter
    private final List<ValueGroup> valueGroups;

    public Mod(String n, String d, Category c, int k) {
        this.name = n;
        this.description = d;
        this.category = c;
        this.keyCode = k;

        this.values = new ArrayList<>();
        this.valueGroups = new ArrayList<>();
    }

    public Mod(String n, String d, Category c) {
        this(n, d, c, Keyboard.KEY_NONE);
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle(boolean state) {
        if (toggled != state) {
            toggled = state;
            if (state) {
                onEnable();
                LiquidCore.getCore().eventManager.register(this);
            } else {
                onDisable();
                LiquidCore.getCore().eventManager.unregister(this);
            }
        }
    }

    public void toggle() {
        toggle(!toggled);
    }

    public void onEnable() {}
    public void onDisable() {}

    public void checkAndRegisterValues() {
        for (Field j : this.getClass().getDeclaredFields()) {
            // Check for value groups first.
            if (!j.isAccessible()) j.setAccessible(true);
            try {
                if (j.get(this) instanceof ValueGroup) {
                    valueGroups.add((ValueGroup) j.get(this));
                    //LiquidCore.getCore().log("[Module Manager] Added group " + ((ValueGroup) j.get(this)).getName());
                }
            } catch (IllegalAccessException e) {
                LiquidCore.getCore().logger.error("An error has occurred while trying to get value group.", e);
            }
        }

        for (Field j : this.getClass().getDeclaredFields()) {
            // Then values.
            try {
                if (j.get(this) instanceof Value) {
                    if (!insideGroups((Value) j.get(this))) {
                        values.add((Value) j.get(this));
                        //LiquidCore.getCore().log("[Module Manager] Added value" + ((Value) j.get(this)).getName());
                    }
                }
            } catch (IllegalAccessException e) {
                LiquidCore.getCore().logger.error("An error has occurred while trying to get value.", e);
            }
        }
    }

    public boolean insideGroups(Value v) {
        for (ValueGroup vs : valueGroups) {
            if (vs.getValues().contains(v))
                return true;
        }
        return false;
    }
}
