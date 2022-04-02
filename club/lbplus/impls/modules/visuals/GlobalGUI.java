package club.lbplus.impls.modules.visuals;

import club.lbplus.cores.module.Category;
import club.lbplus.cores.module.Mod;
import club.lbplus.ui.globalgui.GuiGlobal;
import org.lwjgl.input.Keyboard;

public class GlobalGUI extends Mod {

    public GlobalGUI() {
        super("GlobalGUI", "Manages your scripts, configs, modules, and more.", Category.VISUALS, Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(GuiGlobal.getInstance());
    }

}
