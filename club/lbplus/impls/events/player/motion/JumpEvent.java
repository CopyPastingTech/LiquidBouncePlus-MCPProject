package club.lbplus.impls.events.player.motion;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JumpEvent extends Event {

    private float motion;

}
