package killercreepr.cruxenchants.config;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxenchants.values.DefaultValues;
import killercreepr.cruxenchants.values.ValuesProvider;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class Config extends Cfg implements ValuesProvider {
    public final CfgValue<List<String>> ENCHANTS_TAG_FORMAT = new CommonValue<>(DefaultValues.ENCHANTS_TAG_FORMAT.value()){};
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
    public @NotNull Holder<List<String>> enchantsTagFormat() {
        return ENCHANTS_TAG_FORMAT;
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        setup();
    }
}
