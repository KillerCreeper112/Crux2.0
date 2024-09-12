package killercreepr.crux.block.predicate;

import killercreepr.crux.block.CruxedBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BlockAllPredicate implements BlockPredicate {
    protected final @NotNull Collection<BlockPredicate> children;
    public BlockAllPredicate(@NotNull Collection<BlockPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull CruxedBlock block) {
        for(BlockPredicate predicate : children){
            if(!predicate.test(block)) return false;
        }
        return true;
    }
}
