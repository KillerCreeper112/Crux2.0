package killercreepr.cruxblocks.block.component;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface BushGroup extends CruxBlockGroupComponent {
    @Nullable
    CruxBlock getBlock(@NotNull BushType type);

    Map<BushType, CruxBlock> getBushTypeToBlock();

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
}
