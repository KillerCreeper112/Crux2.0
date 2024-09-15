package killercreepr.crux.persistence.impl;

import killercreepr.crux.Crux;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKeyedTagType<T extends Keyed> implements PersistentDataType<String, T> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext c) {
        return complex.key().asString();
    }

    @Override
    public @NotNull T fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext c) {
        return fromKey(Crux.key(primitive));
    }

    public abstract @NotNull T fromKey(@NotNull Key key);
}
