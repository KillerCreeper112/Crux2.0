package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.api.entity.predicate.EntityPredicate;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityInvertPredicate implements EntityPredicate {
    protected final @NotNull EntityPredicate child;

    public EntityInvertPredicate(@NotNull EntityPredicate child) {
        this.child = child;
    }

    @Override
    public boolean test(@NotNull Entity entity) {
        return !child.test(entity);
    }
}
