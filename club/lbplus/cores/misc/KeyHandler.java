package club.lbplus.cores.misc;

import club.lbplus.impls.events.misc.KeyEvent;
import club.lbplus.LiquidCore;
import club.lbplus.cores.event.EventTarget;

public class KeyHandler {

    @EventTarget
    public void onKey(KeyEvent event) {
        if (!LiquidCore.getCore().isStarting) LiquidCore.getCore().modManager.getModules().stream().filter(it -> it.getKeyCode() == event.getKeyCode()).forEach(m -> m.toggle());
    }

}
