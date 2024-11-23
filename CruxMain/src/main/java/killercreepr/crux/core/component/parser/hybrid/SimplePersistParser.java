package killercreepr.crux.core.component.parser.hybrid;

import killercreepr.crux.api.component.parser.hybrid.PersistParser;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.core.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimplePersistParser<T> implements PersistParser<T> {
    protected final @NotNull Key key;
    protected final @NotNull PersistTextParser<T> textParser;

    public SimplePersistParser(@NotNull Key key, @NotNull PersistTextParser<T> textParser) {
        this.key = key;
        this.textParser = textParser;
    }

    @Override
    public @Nullable T decode(@NotNull PersistentDataContainer from) {
        return CruxTag.get(from, key, dataType(), null);
    }

    @Override
    public void encode(@NotNull PersistentDataContainer to, @Nullable T value) {
        CruxTag.set(to, key, dataType(), value);
    }

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        return textParser.dataType();
    }

    @Override
    public @NotNull Object encodeObject(@NotNull T object) {
        return textParser.encodeObject(object);
    }

    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        return textParser.decodeObject(object);
    }
}
