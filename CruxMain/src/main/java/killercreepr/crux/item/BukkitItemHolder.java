package killercreepr.crux.item;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BukkitItemHolder implements ItemHolder{
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
    public @NotNull Key key() {
        return key;
    }
}
