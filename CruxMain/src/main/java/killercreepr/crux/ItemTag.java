package killercreepr.crux;

import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemTag extends Tag<ItemStack> {
    static ItemTag itemTag(@NotNull Key tagType, @NotNull Key type){
        return new SingleItemTypeTag(tagType, type);
    }
}
