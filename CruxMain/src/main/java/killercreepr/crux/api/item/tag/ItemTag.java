package killercreepr.crux.api.item.tag;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.item.tag.BukkitItemTypeTag;
import killercreepr.crux.core.item.tag.SingleItemTypeTag;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemTag extends Tag<ItemStack> {
    static ItemTag itemTag(@NotNull Key tagType, @NotNull Key type){
        return new SingleItemTypeTag(tagType, type);
    }
    static ItemTag itemTag(org.bukkit.Tag<Material> tag){
        return new BukkitItemTypeTag(tag);
    }
}
