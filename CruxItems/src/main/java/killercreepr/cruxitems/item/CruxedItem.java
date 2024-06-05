package killercreepr.cruxitems.item;

import killercreepr.crux.tags.format.Format;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxedItem extends CruxItem {
    public CruxedItem(@NotNull Material material) {
        super(material);
    }

    public CruxedItem(@NotNull ItemStack item) {
        super(item);
    }

    public CruxedItem(@NotNull Format format, @NotNull Material material) {
        super(format, material);
    }

    public CruxedItem(@NotNull Format format, @NotNull ItemStack item) {
        super(format, item);
    }

    public CruxedItem update(){
        return update((Entity) null);
    }

    public CruxedItem update(@Nullable Entity holder){
        return update(new CruxedItemUpdateContext(this, holder));
    }

    public CruxedItem update(@NotNull CruxedItemUpdateContext context){
        CruxItemRegistries.ITEM_UPDATERS.forEachSorted(updater -> updater.onUpdate(context));
        return this;
    }
}
