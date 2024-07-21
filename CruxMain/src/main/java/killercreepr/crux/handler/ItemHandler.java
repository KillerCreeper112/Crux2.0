package killercreepr.crux.handler;

import killercreepr.crux.item.BukkitItemHolder;
import killercreepr.crux.item.ItemHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemHandler {
    @NotNull
    ItemStack update(@NotNull ItemStack item, @Nullable Entity holder);
    default @NotNull
    ItemStack update(@NotNull ItemStack item){
        return update(item, null);
    }

    @NotNull Key getKey(@NotNull ItemStack item);

    @Nullable
    ItemHolder getItem(@NotNull Key key);

    default boolean compare(@Nullable ItemStack item, @Nullable ItemStack item1){
        if(item == null || item1 == null) return item == item1;
        return compare(getKey(item), item1);
    }

    default boolean compare(@NotNull Key key, @Nullable ItemStack item){
        return item != null && key.equals(getKey(item));
    }


    class Dummy implements ItemHandler {

        @Override
        public @NotNull ItemStack update(@NotNull ItemStack item, @Nullable Entity holder) {
            return item;
        }

        @Override
        public @NotNull Key getKey(@NotNull ItemStack item) {
            return item.getType().getKey();
        }

        @Override
        public @Nullable ItemHolder getItem(@NotNull Key key) {
            Material m = Registry.MATERIAL.get(key);
            if(m == null) return null;
            return new BukkitItemHolder(key);
        }
    }
}
