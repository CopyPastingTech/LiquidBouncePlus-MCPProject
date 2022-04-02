package club.lbplus.impls.events.player.motion;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SlowdownEvent extends Event {

    private float strafe;
    private float forward;

}
