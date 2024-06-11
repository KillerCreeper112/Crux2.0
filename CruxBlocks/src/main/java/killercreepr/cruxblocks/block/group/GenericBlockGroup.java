package killercreepr.cruxblocks.block.group;

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

public abstract class GenericBlockGroup implements CruxBlockGroup{
    protected final @NotNull Key key;
    protected final @NotNull KeyedRegistry<CruxBlock> group = new SimpleKeyedRegistry<>(new LinkedHashMap<>());
    public GenericBlockGroup(@NotNull Key key, @NotNull CruxBlock... blocks) {
        this.key = key;
        for(CruxBlock b : blocks){
            group.register(b);
        }
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
        for(CruxBlock b : group){
            return b;
        }
        throw new UnsupportedOperationException("Block group must have at least one element!");
    }

    @Override
    public @NotNull Collection<CruxBlock> getBlocks() {
        return group.values();
    }

    @Override
    public @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx) {
        return getBaseBlock().placeBlock(ctx);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
