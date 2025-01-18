package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.data.tag.Tag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockTagPredicate implements BlockPredicate, StringListEncodeComponent {
    protected final @NonNull Tag<CruxedBlock> tag;

    public @NonNull Tag<CruxedBlock> getTag() {
        return tag;
    }

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

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of("#" + tag.key().asString());
    }
}
