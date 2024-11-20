package killercreepr.cruxitems.config;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxitems.values.DefaultValues;
import killercreepr.cruxitems.values.ValuesProvider;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class Config extends Cfg implements ValuesProvider {
    public final CfgValue<List<String>> GENERAL_ITEM_LORE_FORMAT = new CommonValue<>(DefaultValues.GENERAL_ITEM_LORE_FORMAT.value()){};
    public final CfgValue<String> GENERAL_ITEM_NAME_FORMAT = new CommonValue<>(DefaultValues.GENERAL_ITEM_NAME_FORMAT.value()){};
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
    public @NotNull Holder<List<String>> generalItemLoreFormat() {
        return GENERAL_ITEM_LORE_FORMAT;
    }

    @Override
    public @NotNull Holder<String> generalItemNameFormat() {
        return GENERAL_ITEM_NAME_FORMAT;
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        setup();
    }
}
