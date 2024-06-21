package killercreepr.cruxblocks.item;

import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxitems.item.plugin.PluginItem;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxItemsItemProvider implements KeyedItemProvider{
    @Override
    public @Nullable ItemStack get(@NotNull Key key, @Nullable Entity holder, @Nullable MergedTagContainer tags) {
        PluginItem pluginItem = CruxItemRegistries.ITEMS.get(key);
        if(pluginItem==null) return null;
        return pluginItem.buildItem(holder, tags);
    }
}
