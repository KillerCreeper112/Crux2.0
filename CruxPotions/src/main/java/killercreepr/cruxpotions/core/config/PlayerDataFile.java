package killercreepr.cruxpotions.core.config;

import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.bukkit.file.JsonCfg;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

public class PlayerDataFile extends JsonCfg {
    public final CfgValue<Collection<StoredPotion>> CRUX_POTIONS = new CommonValue<>(){};
    public PlayerDataFile(@NotNull Plugin plugin, @NotNull UUID uuid) {
        super(plugin, uuid.toString());
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public PlayerDataFile(@NotNull File file) {
        super(file);
    }

    public PlayerDataFile(@NotNull CruxJson cfg) {
        super(cfg);
    }

    public PlayerDataFile(@NotNull File file, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(file, jsonRegistry, reloadIfExists);
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(plugin, path, jsonRegistry, reloadIfExists);
    }

    public PlayerDataFile(@NotNull File file, @NotNull JsonRegistry jsonRegistry) {
        super(file, jsonRegistry);
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry) {
        super(plugin, path, jsonRegistry);
    }

    public PlayerDataFile(@NotNull File file, boolean reloadIfExists) {
        super(file, reloadIfExists);
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists) {
        super(plugin, path, reloadIfExists);
    }
}
