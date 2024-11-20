package killercreepr.cruxblocks.block.group;

import com.google.common.base.Preconditions;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.component.CruxBlockGroupComponent;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.registry.BlockGroupBlocksRegistry;
import killercreepr.cruxblocks.registry.SimpleBlockGroupBlocksRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public class SimpleBlockGroup implements CruxBlockGroup{
    protected final @NotNull Key key;
    protected final @NotNull BlockGroupBlocksRegistry<CruxBlock> group = new SimpleBlockGroupBlocksRegistry<>(this);
    protected final @NotNull CruxBlock baseBlock;
    protected final @NotNull DataComponentHandler components;
    public SimpleBlockGroup(@NotNull Key key, @NotNull CruxBlock... blocks) {
        this(key, DataComponentHandler.simple(), blocks);
    }

    public SimpleBlockGroup(@NotNull Key key, @NotNull DataComponentHandler components, @NotNull CruxBlock... blocks) {
        this.components = components;
        Preconditions.checkArgument(blocks != null && blocks.length > 0, "Block group must have at least one element!");
        this.key = key;
        for(CruxBlock b : blocks){
            b.setGroup(this);
            group.register(b);
        }
        baseBlock = blocks[0];
    }

    @Override
    public boolean canPlace(@NotNull BlockContext ctx) {
        for(CruxBlockGroupComponent parsed : getComponents().getAllOfType(CruxBlockGroupComponent.class)){
            Boolean canPlace = parsed.canPlace(ctx, this);
            if(canPlace != null) return canPlace;
        }
        return CruxBlockGroup.super.canPlace(ctx);
    }

    @Override
    public @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics) {
        for(CruxBlockGroupComponent parsed : getComponents().getAllOfType(CruxBlockGroupComponent.class)){
            ActiveCruxBlock placed = parsed.placeBlock(ctx, applyPhysics, this);
            if(placed != null) return placed;
        }

        return getBaseBlock().placeBlock(ctx, applyPhysics);
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull Predicate<CruxBlock> predicate) {
        for(CruxBlock block : this){
            if(predicate.test(block)) return block;
        }
        return null;
    }

    @Override
    public <T extends CruxBlock> @Nullable T getBlock(@NotNull Class<T> type, @NotNull Predicate<T> predicate) {
        for(CruxBlock block : this){
            if(!type.isAssignableFrom(block.getClass())) continue;
            T casted = type.cast(block);
            if(predicate.test(casted)) return casted;
        }
        return null;
    }

    @Override
    public boolean containsBlock(@NotNull Key key) {
        return getBlock(key) != null;
    }

    @Override
    public boolean containsBlock(@NotNull CruxBlock block) {
        return containsBlock(block.key());
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull Key key) {
        return group.get(key);
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull BlockData data) {
        for(CruxBlock b : group){
            if(b.getTextureData().compareTexture(data)) return b;
        }
        return null;
    }

    @Override
    public @Nullable CruxBlock getBlock(@NotNull Block block) {
        for(CruxBlock b : group){
            if(b.getTextureData().compareTexture(block)) return b;
        }
        return null;
    }

    @Override
    public @NotNull CruxBlock getBaseBlock() {
        return baseBlock;
    }

    @Override
    public @NotNull Collection<CruxBlock> getBlocks() {
        return group.values();
    }

    @Override
    public @NotNull DataComponentHandler getComponents() {
        return components;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
