package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityTagPredicate implements EntityPredicate, StringListEncodeComponent {
    protected final @NonNull Tag<Entity> tag;
    public EntityTagPredicate(@NonNull Tag<Entity> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@NotNull Entity item) {
        return tag.isTagged(item);
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        return List.of("#" + tag.key().asString());
    }
}
