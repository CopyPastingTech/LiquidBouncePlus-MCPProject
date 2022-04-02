package club.lbplus.impls.events.render;

import club.lbplus.cores.event.Event;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;

@Getter
@AllArgsConstructor
public class RenderEntityEvent extends Event {

    @NotNull
    private final Entity entity;

    private final double x;
    private final double y;
    private final double z;
    private final float entityYaw;
    private final float partialTicks;

}
