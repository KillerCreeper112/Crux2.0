package killercreepr.cruxitems.item.plugin;

import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.ItemBuilder;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PluginItem extends Keyed, ItemBuilder {
    @NotNull
    CruxedItem build(@Nullable Entity holder, @Nullable StringHookContainer tags);
    default @NotNull CruxedItem build(@Nullable Entity holder){
        return build(holder, null);
    }
    default @NotNull CruxedItem build(){
        return build(null, null);
    }

    @Override
    default @NotNull ItemStack buildItem(@Nullable Entity holder, @Nullable StringHookContainer tags){
        return build(holder, tags).item();
    }
}
