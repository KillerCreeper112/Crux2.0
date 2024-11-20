package killercreepr.crux.core.item.tag;

import killercreepr.crux.api.item.tag.ItemTag;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BukkitItemTypeTag implements ItemTag {
    protected final @NotNull org.bukkit.Tag<Material> tag;
    public BukkitItemTypeTag(@NotNull Tag<Material> tag) {
        this.tag = tag;
    }

    @Override
    public boolean isTagged(@NotNull ItemStack item) {
        return tag.isTagged(item.getType());
    }

    @Override
    public @NotNull Key key() {
        return tag.key();
    }
}
