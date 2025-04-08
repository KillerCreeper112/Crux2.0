package killercreepr.crux.core.loot.conditions.block;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.loot.conditions.CollectionCondition;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class BlockDirectionalNearbyCondition extends BaseCondition {
    protected final @NotNull BlockPredicate blockPredicate;
    protected final @NotNull Collection<BlockFace> faces;
    public BlockDirectionalNearbyCondition(@NotNull String target, @NotNull BlockPredicate blockPredicate, @NotNull Collection<BlockFace> faces) {
        super(target);
        this.blockPredicate = blockPredicate;
        this.faces = faces;
    }

    public @NotNull BlockPredicate getBlockPredicate() {
        return blockPredicate;
    }

    public @NotNull Collection<BlockFace> getFaces() {
        return faces;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Block b = ctx.info().get(target, Block.class);
        if(b==null) return false;
        for(BlockFace face : faces){
            Block near = b.getRelative(face);
            CruxedBlock cruxed = Crux.handlers().block().getBlock(near);
            if(blockPredicate.test(cruxed)) return true;
        }
        return false;
    }
}
