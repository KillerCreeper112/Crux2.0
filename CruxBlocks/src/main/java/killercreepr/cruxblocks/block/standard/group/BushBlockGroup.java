package killercreepr.cruxblocks.block.standard.group;

import killercreepr.crux.Crux;
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
import java.util.logging.Level;

public abstract class BushBlockGroup extends GenericBlockGroup {
    protected final Map<BushType, BushBlock> bushTypeToBlock = new HashMap<>();
    public BushBlockGroup(@NotNull Key key, @NotNull BushBlock... blocks) {
        super(key, blocks);
        for(BushBlock b : blocks){
            bushTypeToBlock.put(b.getBushType(), b);
        }
    }

    public BushBlockGroup(@NotNull Key key, @NotNull CruxBlock... blocks) {
        super(key, blocks);
        for(CruxBlock b : blocks){
            if(!(b instanceof BushBlock bush)){
                Crux.log(Level.WARNING, "BushBlockGroup created with non-bush block, " + b.key() + ". Ignoring...");
                continue;
            }
            bushTypeToBlock.put(bush.getBushType(), bush);
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

        if(getBlocks().size() > 1){
            check = b.getRelative(BlockFace.UP);
            if(!check.isEmpty() && !check.isReplaceable()) return false;
        }

        if(getBlocks().size() > 2){
            check = check.getRelative(BlockFace.UP);
            if(!check.isEmpty() && !check.isReplaceable()) return false;
        }

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
        Block current = b.getRelative(BlockFace.UP);
        if(middle != null){
            middle.placeBlock(ctx.withBlock(current), false);
            current = current.getRelative(BlockFace.UP);
        }
        top.placeBlock(ctx.withBlock(current), applyPhysics);
        return active;
    }
}
