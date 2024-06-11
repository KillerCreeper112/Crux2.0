package killercreepr.cruxblocks.block.group;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Sound;
import org.bukkit.SoundGroup;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a group of blocks to pretty much fit everything together.
 */
public interface CruxBlockGroup extends Keyed, Iterable<CruxBlock> {
    @Nullable CruxBlock getBlock(@NotNull Key key);
    @Nullable CruxBlock getBlock(@NotNull BlockData data);
    @Nullable CruxBlock getBlock(@NotNull Block block);
    @NotNull CruxBlock getBaseBlock();
    @NotNull Collection<CruxBlock> getBlocks();
    @NotNull
    @Override
    default Iterator<CruxBlock> iterator(){
        return getBlocks().iterator();
    }

    @Nullable
    ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext context);


    float getHardness();

    default SoundGroup getSoundGroup(){
        return defaultSoundGroup();
    }

    default boolean canPlace(@NotNull BlockContext ctx){
        return true;
    }

    static @NotNull SoundGroup defaultSoundGroup(){
        return new SoundGroup() {
            @Override
            public float getVolume() {
                return 1f;
            }

            @Override
            public float getPitch() {
                return 1f;
            }

            @Override
            public @NotNull Sound getBreakSound() {
                return Sound.BLOCK_STONE_BREAK;
            }

            @Override
            public @NotNull Sound getStepSound() {
                return Sound.BLOCK_STONE_STEP;
            }

            @Override
            public @NotNull Sound getPlaceSound() {
                return Sound.BLOCK_STONE_PLACE;
            }

            @Override
            public @NotNull Sound getHitSound() {
                return Sound.BLOCK_STONE_HIT;
            }

            @Override
            public @NotNull Sound getFallSound() {
                return Sound.BLOCK_STONE_FALL;
            }
        };
    }
}
