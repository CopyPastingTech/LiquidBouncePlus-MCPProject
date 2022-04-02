package club.lbplus.impls.events.world;

import club.lbplus.cores.event.Event;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

@Getter
public class CollideEvent extends Event {

    private final double x;
    private final double y;
    private final double z;

    @NotNull
    private final Block block;

    @Setter
    private AxisAlignedBB boundingBox;

    public CollideEvent(final @NotNull BlockPos bp, final @NotNull Block b, AxisAlignedBB bb) {
        this.x = bp.getX();
        this.y = bp.getY();
        this.z = bp.getZ();

        this.block = b;
        this.boundingBox = bb;
    }

}
