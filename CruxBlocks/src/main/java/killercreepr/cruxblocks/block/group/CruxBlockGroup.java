package killercreepr.cruxblocks.block.group;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.CruxBlockData;
import killercreepr.cruxblocks.block.context.BlockContext;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Represents a group of blocks to pretty much fit everything together.
 */
public interface CruxBlockGroup extends Keyed, CruxBlockData, Iterable<CruxBlock> {
    boolean containsBlock(@NotNull Key key);
    boolean containsBlock(@NotNull CruxBlock block);

    @Nullable CruxBlock getBlock(@NotNull Predicate<CruxBlock> predicate);
    <T extends CruxBlock> @Nullable T getBlock(@NotNull Class<T> type, @NotNull Predicate<T> predicate);

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

    @Override
    default CreateBlockSoundGroup getSoundGroup(){
        return null;
    }

    @Override
    default boolean canPlace(@NotNull BlockContext ctx){
        return true;
    }
}
