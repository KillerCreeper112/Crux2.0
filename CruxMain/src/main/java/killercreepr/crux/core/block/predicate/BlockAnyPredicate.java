package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BlockAnyPredicate implements BlockPredicate {
    protected final @NotNull Collection<BlockPredicate> children;
    public BlockAnyPredicate(@NotNull Collection<BlockPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull CruxedBlock block) {
        for(BlockPredicate predicate : children){
            if(predicate.test(block)) return true;
        }
        return false;
    }
}
