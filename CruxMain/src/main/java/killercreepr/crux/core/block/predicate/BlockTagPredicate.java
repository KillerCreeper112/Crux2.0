package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.data.tag.Tag;
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

    @Override
    public String toString() {
        return "BlockTagPredicate{tag=" + tag + "}";
    }
}
