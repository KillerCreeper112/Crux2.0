package killercreepr.crux.api.data.tag;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface Tag<T> extends Keyed {
    boolean isTagged(@NotNull T item);
}
