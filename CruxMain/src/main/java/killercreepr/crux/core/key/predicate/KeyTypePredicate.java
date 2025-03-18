package killercreepr.crux.core.key.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.key.tag.KeyPredicate;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KeyTypePredicate implements KeyPredicate, StringListEncodeComponent {
    protected final @NonNull Key type;
    public KeyTypePredicate(@NonNull Key type) {
        this.type = type;
    }

    public @NonNull Key getType() {
        return type;
    }

    @Override
    public boolean test(@NotNull Key block) {
        return block.equals(type);
    }

    @Override
    public String toString() {
        return "KeyTypePredicate{type=" + type + "}";
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of(type.asString());
    }
}
