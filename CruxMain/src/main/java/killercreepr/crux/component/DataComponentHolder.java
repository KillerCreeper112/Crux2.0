package killercreepr.crux.component;

import javax.annotation.Nullable;

public interface DataComponentHolder {
    DataComponentHandler getComponents();

    @Nullable
    default <T> T get(DataComponentType<? extends T> type) {
        return this.getComponents().get(type);
    }

    default <T> T getOrDefault(DataComponentType<? extends T> type, T fallback) {
        return this.getComponents().getOrDefault(type, fallback);
    }

    default boolean has(DataComponentType<?> type) {
        return this.getComponents().has(type);
    }
}
