package killercreepr.crux.block.predicate;

import killercreepr.crux.block.CruxedBlock;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class BlockTypePredicate implements BlockPredicate {
    protected final @NonNull Key type;
    public BlockTypePredicate(@NonNull Key type) {
        this.type = type;
    }

    @Override
    public boolean test(@NotNull CruxedBlock block) {
        return block.getType().equals(type);
    }
}
