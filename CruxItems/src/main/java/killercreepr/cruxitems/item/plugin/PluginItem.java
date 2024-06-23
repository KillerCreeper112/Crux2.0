package killercreepr.cruxitems.item.plugin;

import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.ItemBuilder;
import killercreepr.cruxitems.persistence.CruxItemsPersistTags;
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
    default CruxedItem build(@Nullable Entity holder, @Nullable MergedTagContainer tags){
        return new CruxedItem(buildItem(holder, tags)).setPluginItem(key());
    }
    default @NotNull CruxedItem build(@Nullable Entity holder){
        return build(holder, null);
    }
    default @NotNull CruxedItem build(){
        return build(null);
    }
}
