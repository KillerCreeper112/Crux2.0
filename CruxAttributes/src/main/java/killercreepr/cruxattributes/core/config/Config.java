package killercreepr.cruxattributes.core.config;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxattributes.api.values.ValuesProvider;
import killercreepr.cruxattributes.core.values.DefaultValues;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class Config extends Cfg implements ValuesProvider {
    public final CfgValue<List<String>> CRUX_ATTRIBUTES_ITEM_FORMAT = new CommonValue<>(DefaultValues.CRUX_ATTRIBUTES_ITEM_FORMAT.value()){};
    public final CfgValue<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT = new CommonValue<>(DefaultValues.CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT.value()){};
    public final CfgValue<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT = new CommonValue<>(DefaultValues.CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT.value()){};
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
    public @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_FORMAT() {
        return CRUX_ATTRIBUTES_ITEM_FORMAT;
    }

    @Override
    public @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT() {
        return CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT;
    }

    @Override
    public @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT() {
        return CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT;
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        setup();
    }

}
