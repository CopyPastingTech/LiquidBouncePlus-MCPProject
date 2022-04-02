package club.lbplus.impls.events.misc;

import club.lbplus.cores.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Getter
@AllArgsConstructor
public class ClickBlockEvent extends Event {

    private final BlockPos clickedBlock;
    private final EnumFacing enumFacing;

}
