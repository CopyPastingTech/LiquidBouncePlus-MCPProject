package club.lbplus.impls.modules.misc;

import club.lbplus.cores.module.Category;
import club.lbplus.cores.module.Mod;
import club.lbplus.impls.values.BoolValue;
import org.lwjgl.input.Keyboard;

public class TestMod extends Mod {

    public TestMod() {
        super("Test", "For testing purpose.", Category.MISC, Keyboard.KEY_R);
    }

    public final BoolValue boolValue = new BoolValue("hii", true);

    @Override
    public void onEnable() {
        mc.thePlayer.sendChatMessage("hi");
    }

    @Override
    public void onDisable() {
        mc.thePlayer.sendChatMessage("bye" + boolValue.get());
    }

}
