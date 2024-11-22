package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicateComponent;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockTypePredicate implements BlockPredicateComponent {
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

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of(type.asString());
    }
}
