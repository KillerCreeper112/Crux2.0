package killercreepr.crux.core.world.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.world.predicate.WorldPredicate;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldKeyPredicate implements WorldPredicate, StringListEncodeComponent {
    protected final @NotNull Key key;
    public WorldKeyPredicate(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public boolean test(@NotNull World entity) {
        return key.equals(entity.key());
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of(key.asString());
    }
}
