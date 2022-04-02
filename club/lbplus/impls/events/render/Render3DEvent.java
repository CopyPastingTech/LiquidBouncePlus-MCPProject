package club.lbplus.impls.events.render;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Render3DEvent extends Event {

    private final float partialTicks;

}
