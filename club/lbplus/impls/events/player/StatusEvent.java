package club.lbplus.impls.events.player;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusEvent extends Event {

    private boolean sprinting;
    private boolean sneaking;

}
