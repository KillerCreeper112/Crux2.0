package killercreepr.cruxitems.item.plugin;

import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.ItemBuilder;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PluginItem extends Keyed, ItemBuilder {
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
