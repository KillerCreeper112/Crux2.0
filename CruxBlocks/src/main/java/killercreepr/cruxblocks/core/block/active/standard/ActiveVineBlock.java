package killercreepr.cruxblocks.core.block.active.standard;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.component.VineGroup;
import killercreepr.cruxblocks.api.block.component.VineType;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.core.block.active.SimpleActiveCruxBlock;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class ActiveVineBlock extends SimpleActiveCruxBlock {
    protected final @NotNull VineType vineType;
    public ActiveVineBlock(@NotNull Block block, @NotNull CruxBlock cruxBlock, @NotNull VineType vineType) {
        super(block, cruxBlock);
        this.vineType = vineType;
    }

    /*@Override
    public @Nullable Collection<ItemStack> getDrops(@Nullable Miner miner) {
        //only the "base" bush block should drop an item
        switch (getVineType()){
            case TOP, MIDDLE ->{ return null; }
        }
        return super.getDrops(miner);
    }*/

    /**
     * Called to basically break the block if it's no longer in a valid location.
     * In the majority of implementations, the block should break if {@link #isPlacementValid()} returns false.
     * For example, a flower would probably want to break if it's no longer on a solid block.
     */
    @Override
    public void update() {
        super.update();
        if(!isValid()) return;

        switch (getVineType()){
            case TOP ->{
                Block up = block.getRelative(BlockFace.UP);
                CruxBlock active = CruxBlocksRegistries.BLOCK.getByBlock(up);
                if(active == null || !cruxBlock.getGroup().containsBlock(active)) return;
                VineGroup group = cruxBlock.getGroup().getComponents().get(CruxBlockComponents.VINE_GROUP);
                group.getBlock(VineType.MIDDLE).setBlock(
                    BlockContext.context(block, null), false, false
                );
            }
            case MIDDLE ->{
                Block up = block.getRelative(BlockFace.UP);
                CruxBlock active = CruxBlocksRegistries.BLOCK.getByBlock(up);
                if(active != null && cruxBlock.getGroup().containsBlock(active)) return;
                VineGroup group = cruxBlock.getGroup().getComponents().get(CruxBlockComponents.VINE_GROUP);
                group.getBlock(VineType.TOP).setBlock(
                    BlockContext.context(block, null), false, false
                );
            }
        }
    }

    @Override
    public boolean isPlacementValid() {
        Block ground = block.getRelative(BlockFace.DOWN);
        CruxBlock active = CruxBlocksRegistries.BLOCK.getByBlock(ground);
        return (active != null && cruxBlock.getGroup().containsBlock(active)) || ground.isSolid();
    }

    public @NotNull VineType getVineType(){
        return vineType;
    }
}
