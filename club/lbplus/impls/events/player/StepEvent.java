package club.lbplus.impls.events.player;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StepEvent extends Event {

    private float stepHeight;

}
