package killercreepr.crux.api.key.tag;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface KeyTypeTag extends KeyTag {
    @NotNull
    Collection<Key> getTypes();
}
