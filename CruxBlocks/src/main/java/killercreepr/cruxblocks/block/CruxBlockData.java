package killercreepr.cruxblocks.block;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import org.bukkit.SoundGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxBlockData {
    float getHardness();
    boolean canPlace(@NotNull BlockContext ctx);
    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext context){
        return placeBlock(context, true);
    }
    @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics);
    @Nullable
    CreateBlockSoundGroup getSoundGroup();
}
