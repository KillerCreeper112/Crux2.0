package killercreepr.crux.core.world.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.api.world.predicate.WorldPredicate;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldTagPredicate implements WorldPredicate, StringListEncodeComponent {
    protected final @NonNull Tag<World> tag;
    public WorldTagPredicate(@NonNull Tag<World> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@NotNull World item) {
        return tag.isTagged(item);
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of("#" + tag.key().asString());
    }
}
