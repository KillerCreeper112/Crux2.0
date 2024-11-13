package killercreepr.cruxadvancements.config;

import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxadvancements.values.DefaultValues;
import killercreepr.cruxadvancements.values.ValuesProvider;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.NumCfgValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Config extends Cfg implements ValuesProvider {
    public final NumCfgValue DEFAULT_MAX_TRACKED_ADVANCEMENTS = new NumCfgValue(DefaultValues.DEFAULT_MAX_TRACKED_ADVANCEMENTS);
    public Config(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public Config(@NotNull File file) {
        super(file);
    }

    public Config(@NotNull CruxConfig cfg) {
        super(cfg);
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        setup();
    }

    @Override
    public @NotNull NumberProvider DEFAULT_MAX_TRACKED_ADVANCEMENTS() {
        return DEFAULT_MAX_TRACKED_ADVANCEMENTS.value();
    }
}
