package killercreepr.crux.api.component.serialization;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PersistentDataSerializer<T> extends ComponentSerializer<PersistentDataContainer, T> {
    static PersistentDataSerializer<String> createString(@NotNull Key key){
        return create(key, PersistentDataType.STRING);
    }
    static PersistentDataSerializer<Boolean> createBoolean(@NotNull Key key){
        return create(key, PersistentDataType.BOOLEAN);
    }
    static <T> PersistentDataSerializer<T> create(@NotNull Key key, @NotNull PersistentDataType<?, T> dataType){
        return new Simple<>(key, dataType);
    }
    static PersistentDataSerializer<Integer> createInt(@NotNull Key key){
        return create(key, PersistentDataType.INTEGER);
    }
    static PersistentDataSerializer<Float> createFloat(@NotNull Key key){
        return create(key, PersistentDataType.FLOAT);
    }
    static PersistentDataSerializer<Double> createDouble(@NotNull Key key){
        return create(key, PersistentDataType.DOUBLE);
    }
    static PersistentDataSerializer<Long> createLong(@NotNull Key key){
        return create(key, PersistentDataType.LONG);
    }
    ////
    static PersistentDataSerializer<String> createString(@NotNull String key){
        return create(key, PersistentDataType.STRING);
    }
    static PersistentDataSerializer<Boolean> createBoolean(@NotNull String key){
        return create(key, PersistentDataType.BOOLEAN);
    }
    static <T> PersistentDataSerializer<T> create(@NotNull String key, @NotNull PersistentDataType<?, T> dataType){
        return create(Crux.key(key), dataType);
    }
    static PersistentDataSerializer<Integer> createInt(@NotNull String key){
        return create(key, PersistentDataType.INTEGER);
    }
    static PersistentDataSerializer<Float> createFloat(@NotNull String key){
        return create(key, PersistentDataType.FLOAT);
    }
    static PersistentDataSerializer<Double> createDouble(@NotNull String key){
        return create(key, PersistentDataType.DOUBLE);
    }
    static PersistentDataSerializer<Long> createLong(@NotNull String key){
        return create(key, PersistentDataType.LONG);
    }

    @Override
    @Nullable
    T decode(@NotNull PersistentDataContainer from);

    @Override
    @Nullable
    T encode(@NotNull PersistentDataContainer to, @Nullable T value);

    @NotNull PersistentDataType<?, T> dataType();

    class Simple<T> implements PersistentDataSerializer<T>{
        protected final @NotNull Key key;
        protected final @NotNull PersistentDataType<?, T> dataType;

        public Simple(@NotNull Key key, @NotNull PersistentDataType<?, T> dataType) {
            this.key = key;
            this.dataType = dataType;
        }

        @Override
        public @Nullable T decode(@NotNull PersistentDataContainer from) {
            return CruxTag.get(from, key, dataType, null);
        }

        @Override
        public @Nullable T encode(@NotNull PersistentDataContainer to, @Nullable T value) {
            T previousValue = decode(to);
            CruxTag.set(to, key, dataType, value);
            return previousValue;
        }

        @Override
        public @NotNull PersistentDataType<?, T> dataType() {
            return dataType;
        }
    }
}
