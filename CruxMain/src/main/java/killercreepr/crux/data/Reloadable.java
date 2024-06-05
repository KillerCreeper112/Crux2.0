package killercreepr.crux.data;

import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

public interface Reloadable {
    void reload(@NotNull CruxPlugin plugin);
}
