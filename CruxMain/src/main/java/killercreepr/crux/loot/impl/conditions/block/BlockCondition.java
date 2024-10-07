package killercreepr.crux.loot.impl.conditions.block;

import killercreepr.crux.Crux;
import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.block.predicate.BlockPredicate;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.impl.conditions.BaseCondition;
import org.bukkit.block.Block;
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
        if(b==null) return false;
        CruxedBlock cruxed = Crux.handlers().block().getBlock(b);
        if(blockPredicate != null && (cruxed == null || !blockPredicate.test(cruxed))) return false;
        return true;
    }
}
