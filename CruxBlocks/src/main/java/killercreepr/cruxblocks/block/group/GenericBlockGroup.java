package killercreepr.cruxblocks.block.group;

import com.google.common.base.Preconditions;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.function.Predicate;

public abstract class GenericBlockGroup implements CruxBlockGroup{
    protected final @NotNull Key key;
    protected final @NotNull KeyedRegistry<CruxBlock> group = new SimpleKeyedRegistry<>(new LinkedHashMap<>());
    protected final @NotNull CruxBlock baseBlock;
    public GenericBlockGroup(@NotNull Key key, @NotNull CruxBlock... blocks) {
        Preconditions.checkArgument(blocks != null && blocks.length > 0, "Block group must have at least one element!");
        this.key = key;
        for(CruxBlock b : blocks){
            b.setGroup(this);
            group.register(b);
        }
        baseBlock = blocks[0];
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
    public @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics) {
        return getBaseBlock().placeBlock(ctx, applyPhysics);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
