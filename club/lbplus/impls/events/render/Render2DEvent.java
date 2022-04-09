package club.lbplus.impls.events.render;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.ScaledResolution;

@Getter
@AllArgsConstructor
public class Render2DEvent extends Event {

    private final float partialTicks;
    private final ScaledResolution scaledResolution;

}
