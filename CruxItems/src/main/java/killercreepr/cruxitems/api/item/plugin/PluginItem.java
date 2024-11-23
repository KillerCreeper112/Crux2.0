package killercreepr.cruxitems.api.item.plugin;

import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxitems.api.item.CruxedItem;
import killercreepr.cruxitems.api.item.ItemBuilder;
import killercreepr.cruxitems.core.persistence.CruxItemsPersistTags;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface PluginItem extends Keyed, ItemBuilder {
    static boolean isPluginItem(@Nullable ItemStack item){
        return CruxItemsPersistTags.ITEM.get(item, null) != null;
    }
    static boolean isPluginItem(@Nullable ItemStack item, @NotNull Key key){
        return Objects.equals(
            CruxItemsPersistTags.ITEM.get(item, null), key
        );
    }
    static boolean isPluginItem(@Nullable ItemStack item, @NotNull Keyed keyed){
        return isPluginItem(item, keyed.key());
    }
    @NotNull
    CruxedItem build(@Nullable Entity holder, @Nullable MergedTagContainer tags);
    default @NotNull CruxedItem build(@Nullable Entity holder){
        return build(holder, null);
    }
    default @NotNull CruxedItem build(){
        return build(null);
    }
}
