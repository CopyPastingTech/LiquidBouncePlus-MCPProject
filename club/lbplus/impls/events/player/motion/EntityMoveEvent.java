package club.lbplus.impls.events.player.motion;

import club.lbplus.cores.event.Event;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;

@Getter
@AllArgsConstructor
public class EntityMoveEvent extends Event {

    @NotNull
    private final Entity movedEntity;

}
