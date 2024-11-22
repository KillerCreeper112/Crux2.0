package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class BlockTypePredicate implements BlockPredicate {
    protected final @NonNull Key type;
    public BlockTypePredicate(@NonNull Key type) {
        this.type = type;
    }

    public @NonNull Key getType() {
        return type;
    }

    @Override
    public boolean test(@NotNull CruxedBlock block) {
        return block.getType().equals(type);
    }

    @Override
    public String toString() {
        return "BlockTypePredicate{type=" + type + "}";
    }
}
