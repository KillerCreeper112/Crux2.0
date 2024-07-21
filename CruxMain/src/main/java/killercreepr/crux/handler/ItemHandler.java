package killercreepr.crux.handler;

import net.kyori.adventure.key.Key;
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

    class Dummy implements ItemHandler {

        @Override
        public @NotNull ItemStack update(@NotNull ItemStack item, @Nullable Entity holder) {
            return item;
        }

        @Override
        public @NotNull Key getKey(@NotNull ItemStack item) {
            return item.getType().getKey();
        }
    }
}
