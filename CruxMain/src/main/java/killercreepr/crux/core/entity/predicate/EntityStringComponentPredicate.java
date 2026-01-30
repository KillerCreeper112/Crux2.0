package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityStringComponentPredicate implements EntityPredicate, StringListEncodeComponent {
    public final @NotNull Key type;
    public final String match;
    public final boolean ignoreCase;
    public EntityStringComponentPredicate(@NotNull Key type, String match, boolean ignoreCase) {
        this.type = type;
      this.match = match;
      this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(@NotNull Entity entity) {
        var comp = CruxRegistries.DATA_COMPONENT_TYPE.get(type);
        if(comp == null){
            Crux.logWarning("No data component type of " + type + "! " + getClass().getName());
            return false;
        }
        var crux = CruxEntity.entity(entity);
        var got = crux.get(comp);
        return ignoreCase ? match.equalsIgnoreCase(got + "") : match.equals(got + "");
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of(type.asString());
    }
}
