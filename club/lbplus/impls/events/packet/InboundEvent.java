package club.lbplus.impls.events.packet;

import club.lbplus.cores.event.Event;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;

@Getter
@AllArgsConstructor
public class InboundEvent extends Event {

    @NotNull
    private final Packet<INetHandlerPlayClient> packet;

}
