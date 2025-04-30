package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityTypePredicate implements EntityPredicate, StringListEncodeComponent {
    protected final @NotNull Key type;
    public EntityTypePredicate(@NotNull Key type) {
        this.type = type;
    }

    @Override
    public boolean test(@NotNull Entity entity) {
        return type.equals(Crux.handlers().entity().getType(entity));
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of(type.asString());
    }
}
