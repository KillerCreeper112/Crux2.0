package killercreepr.cruxblocks.block.group;

import killercreepr.crux.api.component.DataComponentHandler;
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
import java.util.function.Predicate;

public class SingularBlockGroup implements CruxBlockGroup{
    protected final @NotNull Key key;
    protected final @NotNull CruxBlock block;
    protected final @NotNull DataComponentHandler components;
    public SingularBlockGroup(@NotNull Key key, @NotNull CruxBlock block, @NotNull DataComponentHandler components) {
        this.key = key;
        this.block = block;
        this.components = components;
        block.setGroup(this);
    }

    public SingularBlockGroup(@NotNull CruxBlock block, @NotNull DataComponentHandler components) {
        this(block.key(), block, components);
    }

    @Override
    public boolean containsBlock(@NotNull Key key) {
        return block.key().equals(key);
    }

    @Override
    public boolean containsBlock(@NotNull CruxBlock block) {
        return containsBlock(block.key());
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull Predicate<CruxBlock> predicate) {
        return predicate.test(block) ? block : null;
    }

    @Override
    public <T extends CruxBlock> @Nullable T getBlock(@NotNull Class<T> type, @NotNull Predicate<T> predicate) {
        if(!type.isAssignableFrom(block.getClass())) return null;
        T casted = type.cast(block);
        return predicate.test(casted) ? casted : null;
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
    public @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext context, boolean applyPhysics) {
        return block.placeBlock(context, applyPhysics);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull DataComponentHandler getComponents() {
        return components;
    }
}
