package killercreepr.crux.api.persistence;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.component.serialization.ComponentSerializer;
import killercreepr.crux.core.registries.CruxRegistries;
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
        if(type instanceof DataComponentType.Notify<? super T> notify){
            if(value == null){
                notify.onComponentRemoved(this, (T) previousValue);
            }else notify.onComponentApplied(this, value, (T) previousValue);
        }
        return (T) previousValue;
    }
}
