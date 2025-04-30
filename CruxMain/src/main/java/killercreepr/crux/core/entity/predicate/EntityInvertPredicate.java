package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
