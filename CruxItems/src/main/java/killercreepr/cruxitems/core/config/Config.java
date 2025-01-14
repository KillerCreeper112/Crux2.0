package killercreepr.cruxitems.core.config;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItemType;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxitems.api.values.ValuesProvider;
import killercreepr.cruxitems.core.item.CfgItemType;
import killercreepr.cruxitems.core.values.DefaultValues;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Config extends Cfg implements ValuesProvider {
    public final CfgValue<List<String>> GENERAL_ITEM_LORE_FORMAT = new CommonValue<>(DefaultValues.GENERAL_ITEM_LORE_FORMAT.value()){};
    public final CfgValue<String> GENERAL_ITEM_NAME_FORMAT = new CommonValue<>(DefaultValues.GENERAL_ITEM_NAME_FORMAT.value()){};
    public final CfgValue<Map<Key, CfgItemType>> ITEM_TYPES = new CommonValue<>(){};
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
    public @NotNull Holder<Map<Key, CfgItemType>> itemTypes() {
        return ITEM_TYPES;
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        setup();
    }
}
