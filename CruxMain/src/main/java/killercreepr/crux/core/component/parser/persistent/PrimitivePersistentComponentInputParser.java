package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.persistent.ComponentInputField;
import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import killercreepr.crux.core.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimitivePersistentComponentInputParser<T> implements PersistentTextParser<T> {
    protected final @NotNull Key key;
    protected final @NotNull ComponentInputField<T> field;

    public PrimitivePersistentComponentInputParser(@NotNull Key key,
                                                   @NotNull ComponentInputField<T> field) {
        this.key = key;
        this.field = field;
    }

    public @NotNull Key getKey() {
        return key;
    }


    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        return (T) field.textInputParser().decodeObject(object);
    }

    @Override
    public @Nullable T decode(@NotNull PersistentDataContainer from) {
        return (T) CruxTag.get(from, key, field.dataType(), null);
    }

    public @NotNull Object encodeFromObject(@NotNull T object){
        return field.textInputParser().encodeObjectUnchecked(object);
    }

    @Override
    public @Nullable T encode(@NotNull PersistentDataContainer to, @Nullable T value) {
        T previousValue = decode(to);
        if(value == null){
            CruxTag.remove(to, key);
            return previousValue;
        }
        PersistentDataType<?, Object> dataType = (PersistentDataType<?, Object>) dataType();
        Object map = encodeFromObject(value);
        CruxTag.set(to, key, dataType, map);
        return previousValue;
    }

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        return (PersistentDataType<?, T>) field.dataType();
    }
}
