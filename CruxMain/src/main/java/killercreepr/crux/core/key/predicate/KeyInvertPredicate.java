package killercreepr.crux.core.key.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.key.tag.KeyPredicate;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class KeyInvertPredicate implements KeyPredicate {
    protected final @NotNull KeyPredicate child;
    public KeyInvertPredicate(@NotNull KeyPredicate child) {
        this.child = child;
    }

    @Override
    public boolean test(@NotNull Key block) {
        return !child.test(block);
    }
}
