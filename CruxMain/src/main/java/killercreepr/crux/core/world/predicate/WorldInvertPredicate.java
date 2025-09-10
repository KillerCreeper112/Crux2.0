package killercreepr.crux.core.world.predicate;

import killercreepr.crux.api.world.predicate.WorldPredicate;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class WorldInvertPredicate implements WorldPredicate {
    protected final @NotNull WorldPredicate child;

    public WorldInvertPredicate(@NotNull WorldPredicate child) {
        this.child = child;
    }

    @Override
    public boolean test(@NotNull World entity) {
        return !child.test(entity);
    }
}
