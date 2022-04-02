package club.lbplus.impls.events.player.motion;

import club.lbplus.cores.event.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveEvent extends Event {

    private double x;
    private double y;
    private double z;

    private boolean safeWalk = false;

    public MoveEvent(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void zero() {
        x = y = z = 0.0;
    }

    public void zeroXZ() {
        x = z = 0.0;
    }

}
