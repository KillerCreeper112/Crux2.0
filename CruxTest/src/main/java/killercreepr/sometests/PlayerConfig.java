package killercreepr.sometests;

import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.bukkit.file.JsonCfg;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class PlayerConfig extends JsonCfg {
    public final CfgValue<PotionEffect> POTION = new CommonValue<>(
            new PotionEffect(PotionEffectType.HEALTH_BOOST, 100, 1)
    ){};
    public PlayerConfig(@NotNull Plugin plugin, @NotNull UUID uuid) {
        super(plugin, uuid.toString());
    }

    public PlayerConfig(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public PlayerConfig(@NotNull File file) {
        super(file);
    }

    public PlayerConfig(@NotNull CruxJson cfg) {
        super(cfg);
    }

    public PlayerConfig(@NotNull File file, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(file, jsonRegistry, reloadIfExists);
    }

    public PlayerConfig(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(plugin, path, jsonRegistry, reloadIfExists);
    }

    public PlayerConfig(@NotNull File file, @NotNull JsonRegistry jsonRegistry) {
        super(file, jsonRegistry);
    }

    public PlayerConfig(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry) {
        super(plugin, path, jsonRegistry);
    }

    public PlayerConfig(@NotNull File file, boolean reloadIfExists) {
        super(file, reloadIfExists);
    }

    public PlayerConfig(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists) {
        super(plugin, path, reloadIfExists);
    }
}
