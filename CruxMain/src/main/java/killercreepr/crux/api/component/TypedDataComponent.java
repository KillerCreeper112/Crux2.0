package killercreepr.crux.api.component;

import killercreepr.crux.core.component.SimpleTypedDataComponent;
import org.jetbrains.annotations.NotNull;

public interface TypedDataComponent<T> {
    static <T> TypedDataComponent<T> createUnchecked(DataComponentType<T> type, Object value){
        return new SimpleTypedDataComponent<>(type, (T) value);
    }

    static <T> TypedDataComponent<T> create(DataComponentType<T> type, T value){
        return new SimpleTypedDataComponent<>(type, value);
    }

    @NotNull
    DataComponentType<T> getType();
    T getValue();

    default <E extends DataComponentEditor> E applyTo(E components) {
        components.set(getType(), getValue());
        return components;
    }
}
