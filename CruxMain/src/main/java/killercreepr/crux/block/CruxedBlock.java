package killercreepr.crux.block;

import killercreepr.crux.data.world.CruxPosition;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface CruxedBlock {
    @NotNull
    Key getType();

    @NotNull CruxPosition getPosition();
    int getX();
    int getY();
    int getZ();
    @ApiStatus.Experimental
    @NotNull
    Block getBlock();
}
