package killercreepr.crux.core.item.tag;

import killercreepr.crux.api.item.ItemListHolder;
import killercreepr.crux.api.item.tag.ItemTag;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BukkitItemTypeTag implements ItemTag, ItemListHolder {
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

    @Override
    public @NotNull List<ItemStack> getItemValues() {
        List<ItemStack> list = new ArrayList<>();
        for(Material material : tag.getValues()){
            if(material.isItem()){
                list.add(new ItemStack(material));
            }
        }
        return list;
    }
}
