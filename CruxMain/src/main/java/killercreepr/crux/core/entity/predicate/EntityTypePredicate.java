package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityTypePredicate implements EntityPredicate {
    protected final @NotNull Key type;
    public EntityTypePredicate(@NotNull Key type) {
        this.type = type;
    }

    @Override
    public boolean test(@NotNull Entity entity) {
        return type.equals(Crux.handlers().entity().getType(entity));
    }
}
