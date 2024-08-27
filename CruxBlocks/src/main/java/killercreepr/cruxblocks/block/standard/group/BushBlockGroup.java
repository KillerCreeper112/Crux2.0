package killercreepr.cruxblocks.block.standard.group;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.block.group.GenericBlockGroup;
import killercreepr.cruxblocks.block.standard.BushBlock;
import killercreepr.cruxblocks.block.standard.BushType;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class BushBlockGroup extends GenericBlockGroup {
    protected final Map<BushType, BushBlock> bushTypeToBlock = new HashMap<>();
    public BushBlockGroup(@NotNull Key key, @NotNull BushBlock... blocks) {
        super(key, blocks);
        for(BushBlock b : blocks){
            bushTypeToBlock.put(b.getBushType(), b);
        }
    }

    public @Nullable BushBlock getBlock(@NotNull BushType type){
        return bushTypeToBlock.get(type);
    }

    public Map<BushType, BushBlock> getBushTypeToBlock() {
        return bushTypeToBlock;
    }

    @Override
    public boolean canPlace(@NotNull BlockContext ctx) {
        Block b = ctx.getBlock();
        Block check = b;
        if(!check.isEmpty() && !check.isReplaceable()) return false;

        check = b.getRelative(BlockFace.UP);
        if(!check.isEmpty() && !check.isReplaceable()) return false;

        check = check.getRelative(BlockFace.UP);
        if(!check.isEmpty() && !check.isReplaceable()) return false;

        return isValidGround(b.getRelative(BlockFace.DOWN));
    }

    public boolean isValidGround(@NotNull Block block){
        return block.isSolid();
    }

    @Override
    public @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics) {
        CruxBlock middle = getBlock(BushType.MIDDLE);
        CruxBlock top = getBlock(BushType.TOP);

        Block b = ctx.getBlock();
        ActiveCruxBlock active = getBaseBlock().placeBlock(ctx, false);
        middle.placeBlock(ctx.withBlock(b.getRelative(BlockFace.UP)), false);
        top.placeBlock(ctx.withBlock(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP)), applyPhysics);
        return active;
    }
}
