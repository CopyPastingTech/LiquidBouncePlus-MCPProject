package club.lbplus.impls.modules.misc;

import club.lbplus.cores.event.EventTarget;
import club.lbplus.cores.module.Category;
import club.lbplus.cores.module.Mod;
import club.lbplus.impls.events.render.RenderTooltipEvent;
import club.lbplus.impls.values.BoolValue;
import net.minecraft.client.gui.Gui;
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

    @EventTarget
    public void onRenderTooltip(RenderTooltipEvent event) {
        Gui.drawRect(200, 200, 300, 150, -1);
    }

}
