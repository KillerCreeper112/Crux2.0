package killercreepr.cruxitems;

import killercreepr.crux.Crux;
import killercreepr.crux.handler.ItemHandler;
import killercreepr.crux.item.BukkitItemHolder;
import killercreepr.crux.item.ItemHolder;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxitems.command.CruxItemsCommands;
import killercreepr.cruxitems.config.Config;
import killercreepr.cruxitems.config.CruxItemsConfigHook;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.GeneralCruxedItemDisplayUpdater;
import killercreepr.cruxitems.item.ItemDisplayFormatter;
import killercreepr.cruxitems.item.PluginItemHolder;
import killercreepr.cruxitems.item.plugin.PluginItem;
import killercreepr.cruxitems.listener.DisableRecipesListener;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import killercreepr.cruxitems.values.DefaultValues;
import killercreepr.cruxitems.values.ValuesProvider;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxItemsModule implements CruxModule, ItemHandler {
    public static final String NAMESPACE = StandardModules.CRUX_ITEMS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected ValuesProvider values;

    public ValuesProvider values() {
        return values;
    }

    public void values(@NotNull ValuesProvider values) {
        this.values = values;
    }

    public CruxItemsModule registerGeneralDisplayFormatter(){
        return registerGeneralDisplayFormatter(1);
    }

    public CruxItemsModule registerGeneralDisplayFormatter(int priority){
        CruxItemRegistries.ITEM_UPDATERS.register(priority, new GeneralCruxedItemDisplayUpdater(new ItemDisplayFormatter.General(values)));
        return this;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxItemsConfigHook.register();
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            values(new Config(plugin, "module/item"));
        }else values(new DefaultValues());

        Crux.handlers().setItem(this);

        plugin.registerListeners(
            new DisableRecipesListener()
        );

        CruxItemsCommands.register(plugin);
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        if(values != null) values.reload(plugin);
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxItemsConfigHook.loadCfgPluginItems(plugin, "items");
        }
    }

    @Override
    public @NotNull ItemStack update(@NotNull ItemStack item, @Nullable Entity holder) {
        return new CruxedItem(item).update(holder).item();
    }

    @Override
    public @NotNull Key getKey(@NotNull ItemStack item) {
        Key key = new CruxedItem(item).getPluginItemKey();
        if(key != null) return key;
        return item.getType().getKey();
    }

    @Override
    public @Nullable ItemHolder getItem(@NotNull Key key) {
        PluginItem pluginItem = CruxItemRegistries.ITEMS.get(key);
        if(pluginItem != null) return new PluginItemHolder(key);
        Material m = Registry.MATERIAL.get(key);
        if(m == null) return null;
        return new BukkitItemHolder(key);
    }
}
