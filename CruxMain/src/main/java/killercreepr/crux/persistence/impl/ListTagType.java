package killercreepr.crux.persistence.impl;

import killercreepr.crux.util.CruxTag;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListTagType<T> implements PersistentDataType<PersistentDataContainer, List<T>> {
    protected final @NotNull PersistentDataType<?, T> tagType;
    public ListTagType(@NotNull PersistentDataType<?, T> tagType) {
        this.tagType = tagType;
    }

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<List<T>> getComplexType() {
        return (Class<List<T>>) (Class<?>) List.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull List<T> complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        int index = -1;
        for(T t : complex){
            CruxTag.set(c, index+"", tagType, t);
        }
        return c;
    }

    @Override
    public @NotNull List<T> fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        List<T> list = new ArrayList<>();
        for(NamespacedKey key : primitive.getKeys()){
            T object = CruxTag.get(primitive, key, tagType, null);
            if(object==null) continue;
            list.add(object);
        }
        return list;
    }

}
