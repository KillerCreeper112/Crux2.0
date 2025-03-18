package killercreepr.crux.core.key.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.api.key.tag.KeyPredicate;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KeyTagPredicate implements KeyPredicate, StringListEncodeComponent {
    protected final @NonNull Tag<Key> tag;

    public @NonNull Tag<Key> getTag() {
        return tag;
    }

    public KeyTagPredicate(@NonNull Tag<Key> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@NotNull Key item) {
        return tag.isTagged(item);
    }

    @Override
    public String toString() {
        return "KeyTagPredicate{tag=" + tag + "}";
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of("#" + tag.key().asString());
    }
}
