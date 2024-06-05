package killercreepr.cruxitems;

import killercreepr.crux.Crux;
import killercreepr.crux.item.ItemUpdater;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxitems.config.Config;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.GeneralCruxedItemDisplayUpdater;
import killercreepr.cruxitems.item.ItemDisplayFormatter;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import killercreepr.cruxitems.values.DefaultValues;
import killercreepr.cruxitems.values.ValuesProvider;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxItemsModule implements CruxModule, ItemUpdater {
    public static final String NAMESPACE = "CruxItems";
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
    public void onEnable(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey("CruxConfigs")){
            values(new Config(plugin, "module/item"));
        }else values(new DefaultValues());

        Crux.setItemUpdater(this);
    }

    @Override
    public @NotNull ItemStack update(@NotNull ItemStack item, @Nullable Entity holder) {
        return new CruxedItem(item).update(holder).item();
    }
}
