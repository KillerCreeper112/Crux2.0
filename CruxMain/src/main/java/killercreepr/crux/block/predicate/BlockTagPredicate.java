package killercreepr.crux.block.predicate;

import killercreepr.crux.data.tag.Tag;
import killercreepr.crux.block.CruxedBlock;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class BlockTagPredicate implements BlockPredicate {
    protected final @NonNull Tag<CruxedBlock> tag;
    public BlockTagPredicate(@NonNull Tag<CruxedBlock> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@NotNull CruxedBlock item) {
        return tag.isTagged(item);
    }
}
