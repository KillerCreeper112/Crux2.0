package killercreepr.crux.api.block;

import killercreepr.crux.api.math.CruxPosition;
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
