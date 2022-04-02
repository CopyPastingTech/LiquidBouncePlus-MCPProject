package club.lbplus.impls.events.packet;

import club.lbplus.cores.event.Event;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;

@Getter
@AllArgsConstructor
public class OutboundEvent extends Event {

    @NotNull
    private final Packet<INetHandlerPlayServer> packet;

}
