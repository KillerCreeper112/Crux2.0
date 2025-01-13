package killercreepr.crux.api.item;

import killercreepr.crux.api.component.serialization.PersistHolderComponentHandler;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.core.item.SimpleCruxItem;
import killercreepr.crux.core.text.format.FormatParserContext;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface CruxItem extends Cloneable, PersistHolderComponentHandler {
    static boolean isEmpty(@Nullable ItemStack i){
        //pre 1.20.5
        //return i == null || i.getType().isEmpty();
        //post 1.20.5
        return i == null || i.isEmpty();
    }
    static int getMaxStackSize(@NotNull ItemStack i){
        ItemMeta meta = i.getItemMeta();
        if(meta==null) return i.getType().getMaxStackSize();
        return meta.hasMaxStackSize() ? meta.getMaxStackSize() : i.getType().getMaxStackSize();
    }

    static int getMaxDurability(@NotNull ItemStack i){
        if(!(i.getItemMeta() instanceof Damageable d)) return 0;
        if(d.hasMaxDamage()) return d.getMaxDamage();
        return i.getType().getMaxDurability();
    }

    static CruxItem wrap(@NotNull ItemStack item){
        return create(item);
    }

    static CruxItem create(@NotNull ItemStack item){
        return new SimpleCruxItem(item);
    }

    static CruxItem create(@NotNull Material item){
        return new SimpleCruxItem(item);
    }

    static CruxItem create(@NotNull FormatSerializer format, @NotNull ItemStack item){
        return new SimpleCruxItem(format, item);
    }

    @NotNull Component NO_ITALICS = Component.empty().decoration(TextDecoration.ITALIC, false);

    @Nullable ItemMeta meta();

    @NotNull Optional<ItemMeta> getMeta();

    @Nullable Key itemModel();
    CruxItem itemModel(@Nullable Key key);
    CruxItem meta(@Nullable ItemMeta meta);

    CruxItem addFlags(@NotNull ItemFlag @NotNull... flags);

    CruxItem removeFlags(@NotNull ItemFlag @NotNull... flags);

    CruxItem addLoreFromString(@Nullable Collection<String> lore);

    @NotNull FormatParserContext buildFormatContext();

    CruxItem addLoreFromString(@NotNull String @Nullable... lore);

    CruxItem addLore(@Nullable Collection<Component> lore);

    CruxItem addLore(@NotNull Component @NotNull... lore);

    @NotNull Material material();

    CruxItem material(@NotNull Material material);

    @Nullable Component displayName();

    CruxItem customName(@Nullable String name);

    CruxItem customName(@Nullable Component name);

    CruxItem itemName(@Nullable String name);

    CruxItem itemName(@Nullable Component name);

    @Nullable List<Component> lore();

    CruxItem loreFromString(@Nullable List<String> lore);

    CruxItem lore(@Nullable List<Component> lore);

    @NotNull Collection<ItemFlag> flags();

    CruxItem flags(@Nullable Collection<ItemFlag> flags);

    @Deprecated(forRemoval = true)
    CruxItem customModelData(@Nullable Integer data);

    boolean unbreakable();

    CruxItem unbreakable(boolean unbreakable);

    CruxItem glow(boolean value);

    CruxItem enchant(@NotNull Enchantment e, int level);

    CruxItem enchant(@NotNull Enchantment e, int level, boolean ignoreLevelRestriction);

    CruxItem amount(int amount);

    int amount();

    @Deprecated(since = "Mojang did weird thing and now hide_attributes does not hide base attributes" +
        " Keep in mind, this function will add a luck attribute to the item and add hide_attributes.")
    CruxItem hideAttributes();

    CruxItem addAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier);

    CruxItem addAttribute(@NotNull Attribute attribute, @NotNull NamespacedKey key, double amount, @NotNull AttributeModifier.Operation operation,
                                 @Nullable EquipmentSlotGroup slot);

    CruxItem color(@Nullable Color color);

    @Nullable Color color();

    CruxItem editMeta(@NotNull Consumer<ItemMeta> consumer);
    CruxItem edit(@NotNull Consumer<ItemStack> consumer);

    <T extends ItemMeta> CruxItem editMeta(@NotNull Class<T> clazz, @NotNull Consumer<T> consumer);

    @NotNull ItemStack item();

    CruxItem item(@NotNull ItemStack item);

    @NotNull FormatSerializer getFormat();
}
