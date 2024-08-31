package killercreepr.crux.handler;

import killercreepr.crux.item.BukkitItemHolder;
import killercreepr.crux.item.ItemHolder;
import killercreepr.crux.util.CruxItem;
import net.kyori.adventure.key.Key;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemHandler {
    @NotNull
    ItemStack update(@NotNull ItemStack item, @Nullable Entity holder);
    default @NotNull
    ItemStack update(@NotNull ItemStack item){
        return update(item, null);
    }

    @NotNull Key getType(@NotNull ItemStack item);

    @Nullable
    ItemHolder getItem(@NotNull Key key);


    default ItemStack damageItem(@NotNull ItemStack item, int amount, @Nullable Entity holder){
        if(holder instanceof LivingEntity e){
            return e.damageItemStack(item, amount);
        }
        if(!(item.getItemMeta() instanceof Damageable meta)) return item;
        int maxDamage = CruxItem.getMaxDurability(item);
        int damage = Math.min(meta.getDamage() + amount, maxDamage);

        meta.setDamage(damage);
        item.setItemMeta(meta);
        if(meta.getDamage() >= maxDamage){
            item.setAmount(item.getAmount() - 1);
            if(holder != null){
                holder.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
            }
        }
        return item;
    }

    default boolean compare(@Nullable ItemStack item, @Nullable ItemStack item1){
        if(item == null || item1 == null) return item == item1;
        return compare(getType(item), item1);
    }

    default boolean compare(@NotNull Key key, @Nullable ItemStack item){
        return item != null && key.equals(getType(item));
    }


    class Dummy implements ItemHandler {

        @Override
        public @NotNull ItemStack update(@NotNull ItemStack item, @Nullable Entity holder) {
            return item;
        }

        @Override
        public @NotNull Key getType(@NotNull ItemStack item) {
            return item.getType().getKey();
        }

        @Override
        public @Nullable ItemHolder getItem(@NotNull Key key) {
            Material m = Registry.MATERIAL.get(key);
            if(m == null) return null;
            return new BukkitItemHolder(key);
        }
    }
}
