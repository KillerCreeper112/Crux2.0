package killercreepr.crux.api.data;

import killercreepr.crux.core.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

public interface Reloadable {
    default void reload(@NotNull CruxPlugin plugin){};
}
