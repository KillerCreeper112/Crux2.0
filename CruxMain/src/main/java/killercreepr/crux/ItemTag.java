package killercreepr.crux;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class ItemTag extends BaseTag<ItemStack> {
    protected final @NotNull Collection<Material> bukkitMaterials;
    public ItemTag(@NotNull Key key, @NotNull Collection<Material> bukkitMaterials) {
        super(key);
        this.bukkitMaterials = bukkitMaterials;
    }

    @Override
    public boolean isTagged(@NotNull ItemStack item) {
        return bukkitMaterials.contains(item.getType());
    }

    @Override
    public @NotNull Collection<ItemStack> getValues() {
        Collection<ItemStack> list = new HashSet<>();
        bukkitMaterials.forEach(m ->{
            if(!m.isItem()) return;
            list.add(new ItemStack(m));
        });
        return list;
    }

    public @NotNull Collection<Material> getBukkitMaterials() {
        return bukkitMaterials;
    }


    public static class Builder {
        protected @NotNull Collection<Material> bukkitMaterials = new HashSet<>();
        protected @NotNull Key key;

        public Builder add(Material... m){
            bukkitMaterials.addAll(Arrays.asList(m));
            return this;
        }

        public Builder bukkitMaterials(Collection<Material> bukkitMaterials) {
            this.bukkitMaterials = bukkitMaterials;
            return this;
        }

        public Builder key(Key key) {
            this.key = key;
            return this;
        }

        public ItemTag build() {
            return new ItemTag(key, bukkitMaterials);
        }
    }
}
