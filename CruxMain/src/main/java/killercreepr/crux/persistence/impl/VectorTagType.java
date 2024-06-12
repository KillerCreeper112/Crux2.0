package killercreepr.crux.persistence.impl;

import killercreepr.crux.Crux;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VectorTagType implements PersistentDataType<PersistentDataContainer, Vector> {
    public static NamespacedKey k(@NotNull String name){ return Crux.key(name); }
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Vector> getComplexType() {
        return Vector.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Vector complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        c.set(k("x"), PersistentDataType.DOUBLE, complex.getX());
        c.set(k("y"), PersistentDataType.DOUBLE, complex.getY());
        c.set(k("z"), PersistentDataType.DOUBLE, complex.getZ());
        return c;
    }

    @Override
    public @NotNull Vector fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        return new Vector(
            c.getOrDefault(k("x"), PersistentDataType.DOUBLE, 0D),
            c.getOrDefault(k("y"), PersistentDataType.DOUBLE, 0D),
            c.getOrDefault(k("z"), PersistentDataType.DOUBLE, 0D)
        );
    }
}