package killercreepr.cruxitems;

import killercreepr.crux.ItemTag;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.plugin.PluginItem;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class CruxedItemTag extends ItemTag {
    protected final @NotNull Collection<Key> cruxPluginItems;
    public CruxedItemTag(@NotNull Key key, @NotNull Collection<Material> bukkitMaterials, @NotNull Collection<Key> cruxPluginItems) {
        super(key, bukkitMaterials);
        this.cruxPluginItems = cruxPluginItems;
    }

    @Override
    public boolean isTagged(@NotNull ItemStack item) {
        return super.isTagged(item) || cruxPluginItems.contains(new CruxedItem(item).getPluginItemKey());
    }

    @Override
    public @NotNull Collection<ItemStack> getValues() {
        Collection<ItemStack> list = super.getValues();
        cruxPluginItems.forEach(key ->{
            PluginItem pluginItem = CruxItemRegistries.ITEMS.get(key);
            if(pluginItem==null) return;
            list.add(pluginItem.buildItem());
        });
        return list;
    }

    public @NotNull Collection<Key> getCruxPluginItems() {
        return cruxPluginItems;
    }

    public static class Builder extends ItemTag.Builder {
        protected @NotNull Collection<Key> cruxPluginItems = new HashSet<>();
        public Builder add(Key... keys){
            cruxPluginItems.addAll(Arrays.asList(keys));
            return this;
        }
        public CruxedItemTag build() {
            return new CruxedItemTag(key, bukkitMaterials, cruxPluginItems);
        }
    }
}
