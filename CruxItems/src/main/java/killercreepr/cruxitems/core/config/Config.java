package killercreepr.cruxitems.core.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxitems.api.values.ValuesProvider;
import killercreepr.cruxitems.core.item.CfgItemType;
import killercreepr.cruxitems.core.values.DefaultValues;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
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

    /**
     * Loads all config values into memory.
     */
    @Override
    public void register() {
        super.register();
        Collection<CfgItemGrouping> groupList = deserialize("item_type_groupings", new TypeToken<Collection<CfgItemGrouping>>(){}.getType());
        if(groupList==null || groupList.isEmpty()) return;

        Map<Key, CfgItemType> itemTypes = ITEM_TYPES.value();
        if(itemTypes == null){
            itemTypes = new HashMap<>();
            ITEM_TYPES.setValue(itemTypes);
        }
        Map<Key, CfgItemType> finalItemTypes = itemTypes;
        groupList.forEach(g ->{
            Bukkit.broadcastMessage("group=" + g.item);
            g.getItem_types().forEach(key ->{
                finalItemTypes.put(key, g.item);
            });
        });
    }

    public static class CfgItemGrouping{
        protected final Collection<Key> item_types;
        protected final CfgItemType item;

        public CfgItemGrouping(Collection<Key> item_types, CfgItemType item) {
            this.item_types = item_types;
            this.item = item;
        }

        public Collection<Key> getItem_types() {
            return item_types;
        }

        public CfgItemType getItem() {
            return item;
        }
    }
}
