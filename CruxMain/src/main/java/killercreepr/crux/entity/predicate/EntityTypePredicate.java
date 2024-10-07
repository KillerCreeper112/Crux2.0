package killercreepr.crux.entity.predicate;

import killercreepr.crux.Crux;
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
