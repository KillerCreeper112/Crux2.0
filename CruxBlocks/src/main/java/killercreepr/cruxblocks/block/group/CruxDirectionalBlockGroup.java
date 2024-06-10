package killercreepr.cruxblocks.block.group;

import killercreepr.cruxblocks.block.CruxBlockDirectional;
import net.kyori.adventure.key.Key;
import org.bukkit.Axis;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxDirectionalBlockGroup extends CruxBlockGroup{
    @Nullable CruxBlockDirectional get(@NotNull BlockFace direction);
    default@Nullable CruxBlockDirectional get(@NotNull Axis direction){
        return switch (direction){
            case X -> get(BlockFace.EAST);
            case Y -> get(BlockFace.UP);
            case Z -> get(BlockFace.NORTH);
        };
    }

    boolean isOrientable();
    @Nullable CruxBlockDirectional getBlock(BlockFace direction);

    @Nullable CruxBlockDirectional getBlock(@NotNull Key key);
    @Nullable CruxBlockDirectional getBlock(@NotNull BlockData data);
    @Nullable CruxBlockDirectional getBlock(@NotNull Block block);
    @NotNull CruxBlockDirectional getBaseBlock();
}
