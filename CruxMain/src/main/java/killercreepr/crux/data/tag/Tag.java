package killercreepr.crux.data.tag;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Tag<T> extends Keyed {
    boolean isTagged(@NotNull T item);
}
