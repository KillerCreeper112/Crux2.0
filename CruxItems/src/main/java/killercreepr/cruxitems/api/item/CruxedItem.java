package killercreepr.cruxitems.api.item;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.item.CruxedItemUpdateContext;
import killercreepr.cruxitems.core.item.SimpleCruxedItem;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxedItem extends CruxItem {
    static CruxedItem cruxed(@NotNull ItemStack item){
        return new SimpleCruxedItem(item);
    }
    static CruxedItem cruxed(@NotNull Material item){
        return new SimpleCruxedItem(item);
    }
    CruxedItem update();

    CruxedItem update(@Nullable Entity holder);

    CruxedItem update(@NotNull CruxedItemUpdateContext context);

    boolean isPluginItem();

    CruxedItem setPluginItem(@Nullable Key key);

    boolean isPluginItem(@NotNull Key key);

    @Nullable Key getPluginItemKey();

    @Nullable PluginItem getPluginItem();
}
