package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.hybrid.PersistTextInputParser;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class PrimitivePersistTextInputParser<T> implements PersistTextInputParser<T> {
    protected final @NotNull PersistentDataType<?, T> dataType;

    public PrimitivePersistTextInputParser(@NotNull PersistentDataType<?, T> dataType) {
        this.dataType = dataType;
    }

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        return dataType;
    }

    @Override
    public @NotNull Object encodeObject(@NotNull T object) {
        return object;
    }
}
