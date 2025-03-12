package killercreepr.crux.api.component;

import java.util.Collection;
import java.util.function.Consumer;

public interface DataComponentDefaultAccessor {
    <T> T getOrDefaultData(DataComponentType<? extends T> type);
    boolean hasOrDefaultData(DataComponentType<?> type);
    boolean hasDefaultData(DataComponentType<?> type);

    <T> Collection<T> getAllOfTypeOrDefaultData(Class<T> type);
    <T> void forEachAllOfTypeOrDefaultData(Class<T> type, Consumer<T> consumer);
    <T> void forEachOrDefaultData(Consumer<TypedDataComponent<?>> consumer);
    <T> void forEachDefaultData(Consumer<TypedDataComponent<?>> consumer);
    boolean isDataOverridden(DataComponentType<?> type);
}
