package killercreepr.cruxblocks.block;

import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;

public interface CruxBlockGroup extends Iterable<CruxBlock> {
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
}
