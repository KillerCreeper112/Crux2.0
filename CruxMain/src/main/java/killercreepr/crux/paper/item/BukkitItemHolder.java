package killercreepr.crux.paper.item;

import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BukkitItemHolder implements ItemHolder {
    protected final @NotNull Key key;
    public BukkitItemHolder(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull ItemStack value() {
        Material material = org.bukkit.Registry.MATERIAL.get(key);
        Objects.requireNonNull(material, "Material " + key + " does not exist!");
        return new ItemStack(material);
    }

    @Override
    public String toString() {
        return "BukkitItemHolder{" +
            "key=" + key +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BukkitItemHolder that)) return false;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
