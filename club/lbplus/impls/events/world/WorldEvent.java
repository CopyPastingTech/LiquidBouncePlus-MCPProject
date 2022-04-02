package club.lbplus.impls.events.world;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.multiplayer.WorldClient;

@Getter
@AllArgsConstructor
public class WorldEvent extends Event {

    private final WorldClient worldClient;

}
