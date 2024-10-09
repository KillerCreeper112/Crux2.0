package killercreepr.crux.entity.predicate;

import killercreepr.crux.data.tag.Tag;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class EntityTagPredicate implements EntityPredicate {
    protected final @NonNull Tag<Entity> tag;
    public EntityTagPredicate(@NonNull Tag<Entity> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@NotNull Entity item) {
        return tag.isTagged(item);
    }
}
