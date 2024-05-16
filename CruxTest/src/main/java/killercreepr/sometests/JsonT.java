package killercreepr.sometests;

import killercreepr.crux.config.bukkit.CruxJson;
import killercreepr.crux.config.common.json.JsonRegistry;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JsonT extends CruxJson {
    public JsonT(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public JsonT(@NotNull File file) {
        super(file);
    }

    public JsonT(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists) {
        super(plugin, path, reloadIfExists);
    }

    public JsonT(@NotNull File file, boolean reloadIfExists) {
        super(file, reloadIfExists);
    }

    public JsonT(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry) {
        super(plugin, path, jsonRegistry);
    }

    public JsonT(@NotNull File file, @NotNull JsonRegistry jsonRegistry) {
        super(file, jsonRegistry);
    }

    public JsonT(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(plugin, path, jsonRegistry, reloadIfExists);
    }

    public JsonT(@NotNull File file, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(file, jsonRegistry, reloadIfExists);
    }


}
