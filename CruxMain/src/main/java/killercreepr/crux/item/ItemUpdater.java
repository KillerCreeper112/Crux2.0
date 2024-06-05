package killercreepr.crux.item;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemUpdater {
    @NotNull
    ItemStack update(@NotNull ItemStack item, @Nullable Entity holder);
    default @NotNull
    ItemStack update(@NotNull ItemStack item){
        return update(item, null);
    }

    class Dummy implements ItemUpdater{

        @Override
        public @NotNull ItemStack update(@NotNull ItemStack item, @Nullable Entity holder) {
            return item;
        }
    }
}
