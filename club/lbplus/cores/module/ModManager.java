package club.lbplus.cores.module;

import club.lbplus.impls.modules.misc.TestMod;
import club.lbplus.LiquidCore;
import lombok.Getter;

import java.util.ArrayList;

public class ModManager {

    @Getter
    private ArrayList<Mod> modules = new ArrayList<>();

    public ModManager() {
        addAllModules(new TestMod());
    }

    public void addAllModules(Mod... mods) {
        for (Mod m : mods) {
            try {
                addModule(m);
            } catch (IllegalAccessException e) {
                LiquidCore.getCore().logger.error("An error has occurred while trying to register module.", e);
            }
        }
    }

    public void addModule(Mod mod) throws IllegalAccessException {
        modules.add(mod);
        mod.checkAndRegisterValues();
        LiquidCore.getCore().log("Successfully registered " + mod.getName());
    }

}
