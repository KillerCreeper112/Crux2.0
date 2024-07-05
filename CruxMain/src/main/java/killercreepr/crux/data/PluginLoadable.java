package killercreepr.crux.data;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface PluginLoadable {
    void save(@NotNull Plugin plugin);
    void load(@NotNull Plugin plugin);
}
