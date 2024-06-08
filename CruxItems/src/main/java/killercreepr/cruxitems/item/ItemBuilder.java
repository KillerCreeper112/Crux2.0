package killercreepr.cruxitems.item;

import killercreepr.crux.tags.container.StringHookContainer;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemBuilder {
    @NotNull
    ItemStack buildItem(@Nullable Entity holder, @Nullable StringHookContainer tags);
    default @NotNull ItemStack buildItem(@Nullable Entity holder){
        return buildItem(holder, null);
    }
    default @NotNull ItemStack buildItem(){
        return buildItem(null, null);
    }
}
