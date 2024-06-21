package killercreepr.cruxblocks.item;

import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface KeyedItemProvider {
    default @Nullable ItemStack get(@NotNull Key key){
        return get(key, null);
    }
    default @Nullable ItemStack get(@NotNull Key key, @Nullable Entity holder){
        return get(key, holder, null);
    }
    @Nullable ItemStack get(@NotNull Key key, @Nullable Entity holder, @Nullable MergedTagContainer tags);
}
