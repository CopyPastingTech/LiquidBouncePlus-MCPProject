package club.lbplus.impls.events.render;

import club.lbplus.cores.event.Event;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;

@Getter
@AllArgsConstructor
public class UpdateModelEvent extends Event {

    @NotNull
    private final EntityPlayer player;
    @NotNull
    private final ModelPlayer modelPlayer;

}
