package club.lbplus.impls.events.player;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;

@Getter
@AllArgsConstructor
public class AttackEvent extends Event {

    private final Entity targetEntity;

}
