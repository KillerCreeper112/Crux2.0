package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import org.jetbrains.annotations.NotNull;

public class BlockInvertPredicate implements BlockPredicate {
    protected final @NotNull BlockPredicate child;
    public BlockInvertPredicate(@NotNull BlockPredicate child) {
        this.child = child;
    }

    @Override
    public boolean test(@NotNull CruxedBlock block) {
        return !child.test(block);
    }
}
