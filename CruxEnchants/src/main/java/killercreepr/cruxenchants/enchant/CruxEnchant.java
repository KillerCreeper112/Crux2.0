package killercreepr.cruxenchants.enchant;

import killercreepr.crux.Crux;
import killercreepr.crux.registries.Registries;
import killercreepr.crux.util.CruxItem;
import killercreepr.crux.util.CruxKey;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.util.CruxTag;
import killercreepr.cruxenchants.registries.CruxEnchantRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface CruxEnchant extends Keyed {
    static @NotNull NamespacedKey k(@NotNull String name){ return Crux.key(name); }

    static boolean canBeEnchanted(@NotNull ItemStack item){
        for(CruxEnchant e : CruxEnchantRegistries.ENCHANTS){
            if(e.canEnchant(item)) return true;
        }
        return false;
    }

    int getMaxLevel();

    /**
     * @return Whether this enchantment should cause the item it's applied to,
     * to glow.
     */
    default boolean glow(){ return true; }

    default @Nullable String getDescription(){ return null; }

    default @NotNull String getName(){
        return CruxString.toTitleCase(key().value());
    }

    /**
     * Checks if the enchantment may be applied to the given item.
     * This does not check for any conflicting enchants.
     * @param item The item to test.
     * @return True, if the enchantment may be applied to item.
     */
    default boolean canEnchant(@NotNull ItemStack item){ return true; }

    default boolean conflictsWith(@NotNull CruxEnchant other){ return false; }

    /**
     * Called when this enchantment gets applied on an item.
     * @param i The item that the enchantment was applied to.
     */
    default void applied(@NotNull ItemStack i, int previousLevel, int newLevel){}

    /**
     * Called when this enchantment gets removed from an item.
     * @param i The item the enchantment was on.
     */
    default void removed(@NotNull ItemStack i){}

    /**
     * @return The enchants that were removed.
     */
    static Map<CruxEnchant, Integer> clear(@Nullable ItemStack i){
        Map<CruxEnchant, Integer> e = get(i);
        CruxTag.remove(i, k("enchants"));
        if(CruxItem.isEmpty(i)) return e;
        for(CruxEnchant d : e.keySet()) d.removed(i);
        return e;
    }

    static Map<CruxEnchant, Integer> clearStored(@Nullable ItemStack i){
        Map<CruxEnchant, Integer> e = getStored(i);
        CruxTag.remove(i, k("stored_enchants"));
        return e;
    }

    static ItemStack add(@Nullable ItemStack i, @NotNull Map<CruxEnchant, Integer> enchants){
        for(Map.Entry<CruxEnchant, Integer> e : enchants.entrySet()){
            set(i, e.getKey(), e.getValue());
        }
        return i;
    }

    static ItemStack add(@Nullable ItemStack i, @NotNull Map<CruxEnchant, Integer> enchants, boolean ignoreMaxLevel){
        for(Map.Entry<CruxEnchant, Integer> e : enchants.entrySet()){
            set(i, e.getKey(), e.getValue(), ignoreMaxLevel);
        }
        return i;
    }

    static ItemStack addStored(@Nullable ItemStack i, @NotNull Map<CruxEnchant, Integer> enchants){
        for(Map.Entry<CruxEnchant, Integer> e : enchants.entrySet()){
            setStored(i, e.getKey(), e.getValue());
        }
        return i;
    }

    static ItemStack addStored(@Nullable ItemStack i, @NotNull Map<CruxEnchant, Integer> enchants, boolean ignoreMaxLevel){
        for(Map.Entry<CruxEnchant, Integer> e : enchants.entrySet()){
            setStored(i, e.getKey(), e.getValue(), ignoreMaxLevel);
        }
        return i;
    }

    static ItemStack remove(@Nullable ItemStack i, @NotNull CruxEnchant enchant){
        PersistentDataContainer data = getEnchantContainer(i);
        boolean had = remove(i, data, enchant);
        setEnchantContainer(i, data == null || data.isEmpty() ? null : data);
        if(had) enchant.removed(i);
        return i;
    }

    static ItemStack removeStored(@Nullable ItemStack i, @NotNull CruxEnchant enchant){
        PersistentDataContainer data = getStoredEnchantContainer(i);
        remove(i, data, enchant);
        setStoredEnchantContainer(i, data == null || data.isEmpty() ? null : data);
        return i;
    }

    static boolean remove(@Nullable ItemStack i, @Nullable PersistentDataContainer data, @NotNull CruxEnchant enchant){
        if(CruxItem.isEmpty(i) || data == null) return false;
        boolean previouslyHad = data.has(CruxKey.key(enchant.key()));
        data.remove(CruxKey.key(enchant.key()));
        if(data.isEmpty()){
            i.editMeta(m -> m.setEnchantmentGlintOverride(null));
        }else if(enchant.glow()){
            boolean removeGlow = true;
            for(CruxEnchant e : get(data).keySet()){
                if(e.glow()){
                    removeGlow = false;
                    break;
                }
            }
            if(removeGlow){
                i.editMeta(m -> m.setEnchantmentGlintOverride(null));
            }
        }
        return previouslyHad;
    }

    static ItemStack set(@Nullable ItemStack i, @NotNull CruxEnchant enchant, int level){
        return set(i, enchant, level, false);
    }

    static ItemStack set(@Nullable ItemStack i, @NotNull CruxEnchant enchant, int level, boolean ignoreMaxLevel){
        PersistentDataContainer data = getEnchantContainer(i);
        int previousLevel = data == null ? 0 : data.getOrDefault(CruxKey.key(enchant.key()), PersistentDataType.INTEGER, 0);
        data = set(i, data, enchant, level, ignoreMaxLevel);
        if(data != null){
            if(enchant.glow()) i.editMeta(m -> m.setEnchantmentGlintOverride(true));
            setEnchantContainer(i, data);
            enchant.applied(i, previousLevel, level);
        }
        return i;
    }

    static ItemStack setStored(@Nullable ItemStack i, @NotNull CruxEnchant enchant, int level){
        return setStored(i, enchant, level, false);
    }

    static ItemStack setStored(@Nullable ItemStack i, @NotNull CruxEnchant enchant, int level, boolean ignoreMaxLevel){
        PersistentDataContainer data = getStoredEnchantContainer(i);
        data = set(i, data, enchant, level, ignoreMaxLevel);
        if(data != null) setStoredEnchantContainer(i, data);
        return i;
    }

    static @Nullable PersistentDataContainer set(@Nullable ItemStack i, @Nullable PersistentDataContainer data, @NotNull CruxEnchant enchant,
                                                                 int level, boolean ignoreMaxLevel){
        if(CruxItem.isEmpty(i)) return null;
        ItemMeta meta = i.getItemMeta();
        if(meta == null) return null;
        if(data == null) data = meta.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
        if(!ignoreMaxLevel) level = Math.min(level, enchant.getMaxLevel());

        data.set(CruxKey.key(enchant.key()), PersistentDataType.INTEGER, level);
        return data;
    }

    static boolean has(@Nullable ItemStack i, @NotNull CruxEnchant enchant){
        return has(i, enchant.key());
    }

    static boolean has(@Nullable ItemStack i, @NotNull Key enchant){
        PersistentDataContainer data = getEnchantContainer(i);
        return data != null && data.has(CruxKey.key(enchant), PersistentDataType.INTEGER);
    }

    static boolean hasStored(@Nullable ItemStack i, @NotNull CruxEnchant enchant){
        return hasStored(i, enchant.key());
    }

    static boolean hasStored(@Nullable ItemStack i, @NotNull Key enchant){
        PersistentDataContainer data = getStoredEnchantContainer(i);
        return data != null && data.has(CruxKey.key(enchant), PersistentDataType.INTEGER);
    }

    static @NotNull Map<CruxEnchant, Integer> get(@Nullable ItemStack i){
        Map<CruxEnchant, Integer> e = new HashMap<>();
        if(CruxItem.isEmpty(i)) return e;
        return get(getEnchantContainer(i));
    }

    static @NotNull Map<CruxEnchant, Integer> getStored(@Nullable ItemStack i){
        Map<CruxEnchant, Integer> e = new HashMap<>();
        if(CruxItem.isEmpty(i)) return e;
        return get(getStoredEnchantContainer(i));
    }

    static @NotNull Map<CruxEnchant, Integer> get(@Nullable PersistentDataContainer c){
        Map<CruxEnchant, Integer> e = new HashMap<>();
        if(c == null) return e;
        for(NamespacedKey s : c.getKeys()){
            CruxEnchant custom = CruxEnchantRegistries.ENCHANTS.get(s);
            if(custom == null) continue;
            e.put(custom, getLevel(c, custom.key()));
        }
        return e;
    }

    static int getLevel(@Nullable ItemStack i, @NotNull CruxEnchant enchant){
        return getLevel(i, enchant.key());
    }

    static int getLevel(@Nullable ItemStack i, @NotNull Key enchant){
        return getLevel(getEnchantContainer(i), enchant);
    }

    static int getStoredLevel(@Nullable ItemStack i, @NotNull CruxEnchant enchant){
        return getStoredLevel(i, enchant.key());
    }

    static int getStoredLevel(@Nullable ItemStack i, @NotNull Key enchant){
        return getLevel(getStoredEnchantContainer(i), enchant);
    }

    static int getLevel(@Nullable PersistentDataContainer data, @NotNull Key enchant){
        if(data == null) return 0;
        return data.getOrDefault(CruxKey.key(enchant), PersistentDataType.INTEGER, 0);
    }

    static ItemStack setEnchantContainer(@Nullable ItemStack i, @Nullable PersistentDataContainer data){
        return CruxTag.set(i, k("enchants"), PersistentDataType.TAG_CONTAINER, data);
    }

    static ItemStack setStoredEnchantContainer(@Nullable ItemStack i, @Nullable PersistentDataContainer data){
        return CruxTag.set(i, k("stored_enchants"), PersistentDataType.TAG_CONTAINER, data);
    }

    static @Nullable PersistentDataContainer getEnchantContainer(@Nullable ItemStack i){
        if(CruxItem.isEmpty(i)) return null;
        return CruxTag.has(i, k("enchants"), PersistentDataType.TAG_CONTAINER) ?
            CruxTag.get(i, k("enchants"), PersistentDataType.TAG_CONTAINER) : null;
    }

    static @Nullable PersistentDataContainer getStoredEnchantContainer(@Nullable ItemStack i){
        if(CruxItem.isEmpty(i)) return null;
        return CruxTag.has(i, k("stored_enchants"), PersistentDataType.TAG_CONTAINER) ?
            CruxTag.get(i, k("stored_enchants"), PersistentDataType.TAG_CONTAINER) : null;
    }
}
