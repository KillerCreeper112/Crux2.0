package killercreepr.cruxblocks.block.group;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class SingularBlockGroup implements CruxBlockGroup{
    protected final @NotNull Key key;
    protected final @NotNull CruxBlock block;
    public SingularBlockGroup(@NotNull Key key, @NotNull CruxBlock block) {
        this.key = key;
        this.block = block;
    }

    public SingularBlockGroup(@NotNull CruxBlock block) {
        this(block.key(), block);
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull Key key) {
        return block;
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull BlockData data) {
        return block;
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull Block block) {
        return this.block;
    }

    @Override
    public @NotNull CruxBlock getBaseBlock() {
        return block;
    }

    @Override
    public @NotNull Collection<CruxBlock> getBlocks() {
        return List.of(block);
    }

    @Override
    public @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext context) {
        return block.placeBlock(context);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
