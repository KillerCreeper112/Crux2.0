package killercreepr.cruxblocks.block.standard.active;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import killercreepr.cruxblocks.block.standard.BushBlock;
import killercreepr.cruxblocks.block.standard.BushType;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class ActiveBushBlock extends ActiveCruxBlockImpl {
    protected final @NotNull BushBlock bush;
    public ActiveBushBlock(@NotNull Block block, @NotNull BushBlock cruxBlock) {
        super(block, cruxBlock);
        this.bush = cruxBlock;
    }

    @Override
    public boolean isPlacementValid() {
        if(!super.isPlacementValid()) return false;
        switch (getBushType()){
            case BOTTOM, MIDDLE ->{
                Block above = block.getRelative(BlockFace.UP);
                CruxBlock cruxAbove = CruxBlocksRegistries.BLOCKS.getByBlock(above);
                if(cruxAbove == null || !cruxBlock.getGroup().containsBlock(cruxAbove)) return false;
            }
        }
        return true;
    }

    public @NotNull BushType getBushType(){
        return bush.getBushType();
    }
}
