package killercreepr.crux.persistence.impl;

import killercreepr.crux.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CruxKeyTagType implements PersistentDataType<String, Key> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<Key> getComplexType() {
        return Key.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull Key complex, @NotNull PersistentDataAdapterContext context) {
        return complex.asString();
    }

    @Override
    public @NotNull Key fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return Crux.key(primitive);
    }
}
