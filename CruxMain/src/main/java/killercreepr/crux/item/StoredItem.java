package killercreepr.crux.item;

import killercreepr.crux.tags.format.FormatSerializer;
import killercreepr.crux.util.CruxItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Primarily used for json serialization.
 */
public class StoredItem extends CruxItem {
    public StoredItem(@NotNull FormatSerializer format, @NotNull ItemStack item) {
        super(format, item);
    }

    public StoredItem(@NotNull FormatSerializer format, @NotNull Material material) {
        super(format, material);
    }

    public StoredItem(@NotNull ItemStack item) {
        super(item);
    }

    public StoredItem(@NotNull Material material) {
        super(material);
    }
}
