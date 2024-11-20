package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public interface BushGroup extends CruxBlockGroupComponent, Iterable<CruxBlock> {
    @Nullable
    CruxBlock getBlock(@NotNull BushType type);

    @Override
    default @Nullable Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlockGroup group) {
        Block b = ctx.getBlock();
        Block check = b;
        if(!check.isEmpty() && !check.isReplaceable()) return false;

        if(group.getBlocks().size() > 1){
            check = b.getRelative(BlockFace.UP);
            if(!check.isEmpty() && !check.isReplaceable()) return false;
        }

        if(group.getBlocks().size() > 2){
            check = check.getRelative(BlockFace.UP);
            if(!check.isEmpty() && !check.isReplaceable()) return false;
        }

        return b.getRelative(BlockFace.DOWN).isSolid();
    }

    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics, @NotNull CruxBlockGroup group) {
        CruxBlock middle = getBlock(BushType.MIDDLE);
        CruxBlock top = getBlock(BushType.TOP);

        Block b = ctx.getBlock();
        ActiveCruxBlock active = group.getBaseBlock().placeBlock(ctx, false);
        Block current = b.getRelative(BlockFace.UP);
        if(middle != null){
            middle.placeBlock(ctx.withBlock(current), false);
            current = current.getRelative(BlockFace.UP);
        }
        top.placeBlock(ctx.withBlock(current), applyPhysics);
        return active;
    }

    class Simple implements BushGroup{
        protected final @NotNull Map<BushType, CruxBlock> bushTypeToBlock = new HashMap<>();

        @Override
        public void onRegistered(@NotNull CruxBlock block, @NotNull CruxBlockGroup group) {
            BushBlock bush = block.getComponents().get(CruxBlockComponents.BUSH_BLOCK);
            if(bush==null) return;
            bushTypeToBlock.put(bush.getBushType(), block);
        }

        @Override
        public @Nullable CruxBlock getBlock(@NotNull BushType type) {
            return bushTypeToBlock.get(type);
        }

        @NotNull
        @Override
        public Iterator<CruxBlock> iterator() {
            return bushTypeToBlock.values().iterator();
        }
    }
}
