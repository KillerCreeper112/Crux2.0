package killercreepr.crux.api.entity.tag;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface EntityTypeTag extends EntityTag {
    @NotNull
    Collection<Key> getTypes();
}
