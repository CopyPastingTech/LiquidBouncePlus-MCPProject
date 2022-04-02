package club.lbplus.impls.events.misc;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClickWindowEvent extends Event {

    private final int windowId;
    private final int slotId;
    private final int mouseButtonClicked;
    private final int mode;

}
