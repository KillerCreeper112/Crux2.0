package killercreepr.cruxblocks.core.block.active.standard;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.component.BushType;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.block.active.SimpleActiveCruxBlock;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ActiveBushBlock extends SimpleActiveCruxBlock {
    protected final @NotNull BushType bushType;
    public ActiveBushBlock(@NotNull Block block, @NotNull CruxBlock cruxBlock, @NotNull BushType bushType) {
        super(block, cruxBlock);
        this.bushType = bushType;
    }

    @Override
    public @Nullable Collection<ItemStack> getDrops(@Nullable Miner miner) {
        //only the "base" bush block should drop an item
        switch (getBushType()){
            case TOP, MIDDLE ->{ return null; }
        }
        return super.getDrops(miner);
    }

    @Override
    public boolean isPlacementValid() {
        switch (getBushType()){
            case BOTTOM, MIDDLE ->{
                Block above = block.getRelative(BlockFace.UP);
                CruxBlock cruxAbove = CruxBlocksRegistries.BLOCK.getByBlock(above);
                if(cruxAbove == null || !cruxBlock.getGroup().containsBlock(cruxAbove)) return false;
            }
        }
        Block ground = block.getRelative(BlockFace.DOWN);
        if(getBushType() == BushType.BOTTOM) return ground.isSolid();

        CruxBlock active = CruxBlocksRegistries.BLOCK.getByBlock(ground);

        if(active == null) return false;
        return cruxBlock.getGroup().containsBlock(active);
    }

    public @NotNull BushType getBushType(){
        return bushType;
    }
}
