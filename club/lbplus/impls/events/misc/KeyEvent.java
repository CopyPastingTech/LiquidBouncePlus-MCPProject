package club.lbplus.impls.events.misc;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyEvent extends Event {

    private final int keyCode;

}
