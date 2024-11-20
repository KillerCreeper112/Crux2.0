package killercreepr.cruxitems.item;

import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemBuilder {
    @NotNull
    ItemStack buildItem(@Nullable Entity holder, @Nullable MergedTagContainer tags);
    default @NotNull ItemStack buildItem(@Nullable Entity holder){
        return buildItem(holder, null);
    }
    default @NotNull ItemStack buildItem(){
        return buildItem(null, null);
    }
}
