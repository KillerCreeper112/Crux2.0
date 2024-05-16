package killercreepr.crux.util;

import killerceepr.crux.Crux;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxTag {
    public static @NotNull String defaultNamespace(){ return Crux.key("a").getNamespace(); }

    public static @NotNull NamespacedKey defaultKey(@NotNull String name){
        return new NamespacedKey(defaultNamespace(), name);
    }

    public static <P extends PersistentDataHolder, T, Z> P set(@Nullable P e, @NotNull NamespacedKey key,
                                                               @NotNull PersistentDataType<T, Z> type, @Nullable Z value){
        set(e == null ? null : e.getPersistentDataContainer(), key, type, value);
        return e;
    }

    public static <P extends PersistentDataHolder, T, Z> P set(@Nullable P e, @NotNull String name,
                                                               @NotNull PersistentDataType<T, Z> type, @Nullable Z value){
        return set(e, defaultKey(name), type, value);
    }

    public static <P extends PersistentDataHolder, T, Z> @Nullable Z get(@Nullable P e, @NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type){
        return get(e == null ? null : e.getPersistentDataContainer(), key, type);
    }

    public static <P extends PersistentDataHolder, T, Z> @Nullable Z get(@Nullable P e, @NotNull String name, @NotNull PersistentDataType<T, Z> type){
        return get(e, defaultKey(name), type);
    }

    public static <P extends PersistentDataHolder, T, Z> @NotNull Z getOrDefault(@Nullable P e, @NotNull NamespacedKey key,
                                                                        @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue){
        return getOrDefault(e == null ? null : e.getPersistentDataContainer(), key, type, defaultValue);
    }

    public static <P extends PersistentDataHolder, T, Z> @NotNull Z getOrDefault(@Nullable P e, @NotNull String name,
                                                                        @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue){
        return getOrDefault(e, defaultKey(name), type, defaultValue);
    }

    public static <P extends PersistentDataHolder, T, Z> Z get(@Nullable P e, @NotNull NamespacedKey key,
                                                                                 @NotNull PersistentDataType<T, Z> type, @Nullable Z defaultValue){
        return get(e == null ? null : e.getPersistentDataContainer(), key, type, defaultValue);
    }
    public static <P extends PersistentDataHolder, T, Z> Z get(@Nullable P e, @NotNull String name,
                                                                                 @NotNull PersistentDataType<T, Z> type, @Nullable Z defaultValue){
        return get(e, defaultKey(name), type, defaultValue);
    }

    public static <P extends PersistentDataHolder> P remove(@Nullable P e, @NotNull String name){
        return remove(e, defaultKey(name));
    }

    public static <P extends PersistentDataHolder> P remove(@Nullable P e, @NotNull NamespacedKey key){
        remove(e == null ? null : e.getPersistentDataContainer(), key);
        return e;
    }

    public static <P extends PersistentDataHolder, T, Z> boolean has(@Nullable P e, @NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type){
        return has(e == null ? null : e.getPersistentDataContainer(), key, type);
    }

    public static <P extends PersistentDataHolder> boolean has(@Nullable P e, @NotNull NamespacedKey key){
        return has(e == null ? null : e.getPersistentDataContainer(), key);
    }

    public static <P extends PersistentDataHolder, T, Z> boolean has(@Nullable P e, @NotNull String name, @NotNull PersistentDataType<T, Z> type){
        return has(e, defaultKey(name), type);
    }

    public static <P extends PersistentDataHolder> boolean has(@Nullable P e, @NotNull String name){
        return has(e, defaultKey(name));
    }

    public static <P extends PersistentDataHolder> P clear(@Nullable P e){
        clear(e == null ? null : e.getPersistentDataContainer());
        return e;
    }

    //ITEM
    public static <T, Z> ItemStack clear(@Nullable ItemStack e){
        if(e == null) return null;
        ItemMeta meta = e.getItemMeta();
        if(meta == null) return e;
        e.setItemMeta(clear(meta));
        return e;
    }

    public static <T, Z> ItemStack set(@Nullable ItemStack e, @NotNull String name,
                                       @NotNull PersistentDataType<T, Z> type, @Nullable Z value){
        return set(e, defaultKey(name), type, value);
    }

    public static <T, Z> ItemStack set(@Nullable ItemStack e, @NotNull NamespacedKey key,
                                       @NotNull PersistentDataType<T, Z> type, @Nullable Z value){
        if(e == null) return null;
        if(value == null) return remove(e, key);
        ItemMeta meta = e.getItemMeta();
        if(meta == null) return e;
        e.setItemMeta(set(meta, key, type, value));
        return e;
    }

    public static <T, Z> @Nullable Z get(@Nullable ItemStack e, @NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type){
        if(e == null) return null;
        ItemMeta meta = e.getItemMeta();
        if(meta == null) return null;
        return get(meta, key, type);
    }

    public static <T, Z> @Nullable Z get(@Nullable ItemStack e, @NotNull String name, @NotNull PersistentDataType<T, Z> type){
        return get(e, defaultKey(name), type);
    }

    public static <T, Z> Z get(@Nullable ItemStack e, @NotNull NamespacedKey key,
                                        @NotNull PersistentDataType<T, Z> type, @Nullable Z defaultValue){
        if(e == null) return defaultValue;
        ItemMeta meta = e.getItemMeta();
        if(meta == null) return defaultValue;
        return get(meta, key, type, defaultValue);
    }

    public static <T, Z> Z get(@Nullable ItemStack e, @NotNull String name,
                                        @NotNull PersistentDataType<T, Z> type, @Nullable Z defaultValue){
        return get(e, defaultKey(name), type, defaultValue);
    }

    public static ItemStack remove(@Nullable ItemStack e, @NotNull String name){
        return remove(e, defaultKey(name));
    }

    public static ItemStack remove(@Nullable ItemStack e, @NotNull NamespacedKey key){
        if(e == null) return null;
        ItemMeta meta = e.getItemMeta();
        if(meta == null) return e;
        e.setItemMeta(remove(meta, key));
        return e;
    }

    public static <T, Z> boolean has(@Nullable ItemStack e, @NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type){
        return e != null && has(e.getItemMeta(), key, type);
    }

    public static <T, Z> boolean has(@Nullable ItemStack e, @NotNull String name, @NotNull PersistentDataType<T, Z> type){
        return has(e, defaultKey(name), type);
    }

    public static boolean has(@Nullable ItemStack e, @NotNull NamespacedKey key){
        return e != null && has(e.getItemMeta(), key);
    }

    public static boolean has(@Nullable ItemStack e, @NotNull String name){
        return has(e, defaultKey(name));
    }

    public static <P extends PersistentDataHolder, T, Z> @NotNull Z getOrDefault(@Nullable ItemStack e, @NotNull NamespacedKey key,
                                                                                 @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue){
        if(e == null) return defaultValue;
        return getOrDefault(e.getItemMeta(), key, type, defaultValue);
    }

    public static <P extends PersistentDataHolder, T, Z> @NotNull Z getOrDefault(@Nullable ItemStack e, @NotNull String name,
                                                                                 @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue){
        if(e == null) return defaultValue;
        return getOrDefault(e.getItemMeta(), name, type, defaultValue);
    }

    //CONTAINER
    public static <P extends PersistentDataContainer, T, Z> P set(@Nullable P e, @NotNull NamespacedKey key,
                                                                  @NotNull PersistentDataType<T, Z> type, @Nullable Z value){
        if(e == null) return null;
        if(value == null) return remove(e, key);
        e.set(key, type, value);
        return e;
    }

    public static <P extends PersistentDataContainer, T, Z> P set(@Nullable P e, @NotNull String name,
                                                               @NotNull PersistentDataType<T, Z> type, @Nullable Z value){
        return set(e, defaultKey(name), type, value);
    }

    public static <P extends PersistentDataContainer, T, Z> @Nullable Z get(@Nullable P e, @NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type){
        if(e == null) return null;
        return e.get(key, type);
    }

    public static <P extends PersistentDataContainer, T, Z> @Nullable Z get(@Nullable P e, @NotNull String name, @NotNull PersistentDataType<T, Z> type){
        return get(e, defaultKey(name), type);
    }

    public static <P extends PersistentDataContainer, T, Z> @NotNull Z getOrDefault(@Nullable P e, @NotNull NamespacedKey key,
                                                                                 @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue){
        if(e == null) return defaultValue;
        return e.getOrDefault(key, type, defaultValue);
    }

    public static <P extends PersistentDataContainer, T, Z> @NotNull Z getOrDefault(@Nullable P e, @NotNull String name,
                                                                                 @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue){
        return getOrDefault(e, defaultKey(name), type, defaultValue);
    }

    public static <P extends PersistentDataContainer, T, Z> Z get(@Nullable P e, @NotNull NamespacedKey key,
                                                               @NotNull PersistentDataType<T, Z> type, @Nullable Z defaultValue){
        if(e == null) return defaultValue;
        try{
            Z x = e.get(key, type);
            if(x == null) return defaultValue;
            return x;
        }catch (Exception x){ return defaultValue; }
    }
    public static <P extends PersistentDataContainer, T, Z> Z get(@Nullable P e, @NotNull String name,
                                                               @NotNull PersistentDataType<T, Z> type, @Nullable Z defaultValue){
        return get(e, defaultKey(name), type, defaultValue);
    }

    public static <P extends PersistentDataContainer> P remove(@Nullable P e, @NotNull String name){
        return remove(e, defaultKey(name));
    }

    public static <P extends PersistentDataContainer> P remove(@Nullable P e, @NotNull NamespacedKey key){
        if(e == null) return null;
        e.remove(key);
        return e;
    }

    public static <P extends PersistentDataContainer, T, Z> boolean has(@Nullable P e, @NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type){
        return e != null && e.has(key, type);
    }

    public static <P extends PersistentDataContainer> boolean has(@Nullable P e, @NotNull NamespacedKey key){
        return e != null && e.has(key);
    }

    public static <P extends PersistentDataContainer, T, Z> boolean has(@Nullable P e, @NotNull String name, @NotNull PersistentDataType<T, Z> type){
        return has(e, defaultKey(name), type);
    }

    public static <P extends PersistentDataContainer> boolean has(@Nullable P e, @NotNull String name){
        return has(e, defaultKey(name));
    }

    public static <P extends PersistentDataContainer> P clear(@Nullable P e){
        if(e == null) return null;
        for(NamespacedKey k : e.getKeys()){
            e.remove(k);
        }
        return e;
    }
}
