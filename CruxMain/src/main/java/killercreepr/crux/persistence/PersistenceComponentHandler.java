package killercreepr.crux.persistence;

import killercreepr.crux.component.DataComponentHandler;
import killercreepr.crux.component.DataComponentType;
import killercreepr.crux.component.TypedDataComponent;
import killercreepr.crux.component.serialzation.ComponentSerializer;
import killercreepr.crux.registries.CruxRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public interface PersistenceComponentHandler extends DataComponentHandler {
    @Nullable
    PersistentDataContainer getComponentsPersistentContainer();

    void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data);

    @Override
    default  <T> @Nullable T get(DataComponentType<? extends T> type) {
        ComponentSerializer<?, ? extends T> serializer = type.serializer();
        if(serializer == null) return null;
        PersistentDataContainer components = getComponentsPersistentContainer();
        if(components == null) return null;
        return serializer.decodeUnchecked(components);
    }

    @Override
    default Set<DataComponentType<?>> keySet() {
        PersistentDataContainer container = getComponentsPersistentContainer();
        if(container == null) return Set.of();

        Set<DataComponentType<?>> types = new HashSet<>();
        for(NamespacedKey key : container.getKeys()){
            DataComponentType<?> type = CruxRegistries.DATA_COMPONENT_TYPE.get(key);
            if(type != null) types.add(type);
        }
        return types;
    }

    @NotNull
    @Override
    default Iterator<TypedDataComponent<?>> iterator() {
        Set<TypedDataComponent<?>> data = new HashSet<>();
        for(DataComponentType<?> type : keySet()){
            data.add(TypedDataComponent.createUnchecked(type, get(type)));
        }
        return data.iterator();
    }

    @Override
    default <T> @Nullable T set(DataComponentType<? super T> type, @Nullable T value) {
        ComponentSerializer<?, ? super T> serializer = type.serializer();
        if(serializer == null) return null;
        PersistentDataContainer container = getComponentsPersistentContainer();
        if(container == null) return null;

        Object previousValue = get(type);
        serializer.encodeUnchecked(container, value);
        onComponentsPersistentContainerChanged(container);
        return (T) previousValue;
    }
}
