package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public interface VineGroup extends CruxBlockGroupComponent, Iterable<CruxBlock> {
    @Nullable
    CruxBlock getBlock(@NotNull VineType type);

    @Override
    default @Nullable Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlockGroup group) {
        Block ground = ctx.getBlock().getRelative(BlockFace.DOWN);
        CruxBlock active = CruxBlocksRegistries.BLOCK.getByBlock(ground);
        return (active != null && group.containsBlock(active)) || ground.isSolid();
    }

   /* default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics, @NotNull CruxBlockGroup group) {
        CruxBlock middle = getBlock(VineType.MIDDLE);
        CruxBlock top = getBlock(VineType.TOP);

        Block b = ctx.getBlock();
        ActiveCruxBlock active = group.getBaseBlock().placeBlock(ctx, false);
        Block current = b.getRelative(BlockFace.UP);
        if(middle != null){
            middle.placeBlock(ctx.withBlock(current), false);
            current = current.getRelative(BlockFace.UP);
        }
        top.placeBlock(ctx.withBlock(current), false);
        return active;
    }*/

    class Simple implements VineGroup {
        protected final @NotNull Map<VineType, CruxBlock> vineTypeToBlock = new HashMap<>();

        @Override
        public void onRegistered(@NotNull CruxBlock block, @NotNull CruxBlockGroup group) {
            VineBlock bush = block.getComponents().get(CruxBlockComponents.VINE_BLOCK);
            if(bush==null) return;
            vineTypeToBlock.put(bush.getVineType(), block);
        }

        @Override
        public @Nullable CruxBlock getBlock(@NotNull VineType type) {
            return vineTypeToBlock.get(type);
        }

        @NotNull
        @Override
        public Iterator<CruxBlock> iterator() {
            return vineTypeToBlock.values().iterator();
        }
    }
}
