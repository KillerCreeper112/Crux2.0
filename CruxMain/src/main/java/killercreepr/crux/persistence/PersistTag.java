package killercreepr.crux.persistence;

import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.crux.util.CruxTag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public class PersistTag<T> {
    public static final Registry<PersistTag<?>> REGISTRY = SimpleRegistry.fromSet();

    public static <E extends PersistTag<?>> @NotNull E register(@NotNull E e){
        REGISTRY.register(e);
        return e;
    }

    public static final PersistTag<Boolean> IGNORED_MOB_TARGET = register(new PersistTag<>(PersistentDataType.BOOLEAN, "ignored_mob_target"));

    protected final PersistentDataType<?, T> tagType;
    protected final String tagName;
    protected final T defaultValue;
    protected final @Nullable Supplier<Collection<String>> values;
    public PersistTag(@NotNull PersistentDataType<?, T> tagType, @NotNull String tagName){
        this(tagType, tagName, null);
    }

    public PersistTag(@NotNull PersistentDataType<?, T> tagType, @NotNull String tagName, @Nullable T defaultValue){
        this(tagType, tagName, defaultValue, null);
    }

    public PersistTag(@NotNull PersistentDataType<?, T> tagType, @NotNull String tagName, @Nullable T defaultValue, @Nullable Supplier<Collection<String>> values) {
        this.tagType = tagType;
        this.tagName = tagName;
        this.defaultValue = defaultValue;
        this.values = values;
    }

    public @Nullable Supplier<Collection<String>> getValues() {
        return values;
    }
    public @NotNull String tagName(){ return tagName; }
    public @NotNull PersistentDataType<?, T> tagType(){ return tagType; }

    //HOLDER
    public <E extends PersistentDataHolder> T get(@Nullable E e){
        return CruxTag.get(e, tagName(), tagType(), defaultValue);
    }
    public <E extends PersistentDataHolder> T get(@Nullable E e, @Nullable T defaultValue){
        return CruxTag.get(e, tagName(), tagType(), defaultValue);
    }
    public <E extends PersistentDataHolder> E remove(@Nullable E e){
        return CruxTag.remove(e, tagName());
    }
    public <E extends PersistentDataHolder> E set(@Nullable E e, @Nullable T value){
        return set(e, value, (PersistentDataHolder[]) null);
    }
    public <E extends PersistentDataHolder> E set(@Nullable E e, @Nullable T value, @NotNull PersistentDataHolder @Nullable... extraData){
        return CruxTag.set(e, tagName(), tagType(), value);
    }
    public <E extends PersistentDataHolder> boolean has(@Nullable E e){
        return CruxTag.has(e, tagName());
    }

    //ITEM
    public T get(@Nullable ItemStack e){
        return CruxTag.get(e, tagName(), tagType(), defaultValue);
    }
    public T get(@Nullable ItemStack e, @Nullable T defaultValue){
        return CruxTag.get(e, tagName(), tagType(), defaultValue);
    }
    public ItemStack remove(@Nullable ItemStack e){
        return CruxTag.remove(e, tagName());
    }
    public ItemStack set(@Nullable ItemStack e, @Nullable T value){
        return set(e, value, (PersistentDataHolder[]) null);
    }

    public ItemStack set(@Nullable ItemStack e, @Nullable T value, @NotNull PersistentDataHolder @Nullable... extraData){
        return CruxTag.set(e, tagName(), tagType(), value);
    }

    public boolean has(@Nullable ItemStack e){
        return CruxTag.has(e, tagName());
    }

    //CONTAINER
    public <E extends PersistentDataContainer> T get(@Nullable E e){
        return CruxTag.get(e, tagName(), tagType(), defaultValue);
    }
    public <E extends PersistentDataContainer> T get(@Nullable E e, @Nullable T defaultValue){
        return CruxTag.get(e, tagName(), tagType(), defaultValue);
    }
    public <E extends PersistentDataContainer> E remove(@Nullable E e){
        return CruxTag.remove(e, tagName());
    }
    public <E extends PersistentDataContainer> E set(@Nullable E e, @Nullable T value){
        return set(e, value, (PersistentDataHolder[]) null);
    }
    public <E extends PersistentDataContainer> E set(@Nullable E e, @Nullable T value, @NotNull PersistentDataHolder @Nullable... extraData){
        return CruxTag.set(e, tagName(), tagType(), value);
    }
    public <E extends PersistentDataContainer> boolean has(@Nullable E e){
        return CruxTag.has(e, tagName());
    }
}
