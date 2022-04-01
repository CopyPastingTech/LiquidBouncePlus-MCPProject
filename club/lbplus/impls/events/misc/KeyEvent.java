package club.lbplus.impls.events.misc;

import club.lbplus.cores.event.Event;

public class KeyEvent extends Event {
    private final int keyCode;

    public KeyEvent(int code) {
        this.keyCode = code;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
