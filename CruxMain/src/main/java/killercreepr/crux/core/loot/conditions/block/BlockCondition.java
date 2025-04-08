package killercreepr.crux.core.loot.conditions.block;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockCondition extends BaseCondition {
    protected final @Nullable BlockPredicate blockPredicate;
    public BlockCondition(@NotNull String target, @Nullable BlockPredicate blockPredicate) {
        super(target);
        this.blockPredicate = blockPredicate;
    }

    public @Nullable BlockPredicate getBlockPredicate() {
        return blockPredicate;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Block b = ctx.info().get(target, Block.class);
        if(b == null){
            var state = ctx.info().get(target, BlockState.class);
            if(state == null) return false;
            try{
                b = state.getBlock();
            }catch (IllegalStateException ignored){}
        }
        if(b==null) return false;
        CruxedBlock cruxed = Crux.handlers().block().getBlock(b);
        if(blockPredicate != null && (cruxed == null || !blockPredicate.test(cruxed))) return false;
        return true;
    }
}
