package killercreepr.crux.core.persistence.type;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.math.LocationPos;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class LocationPosTagType implements PersistentDataType<PersistentDataContainer, LocationPos> {
    public static NamespacedKey k(@NotNull String name){ return Crux.key(name); }
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<LocationPos> getComplexType() {
        return LocationPos.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull LocationPos complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        c.set(k("x"), PersistentDataType.DOUBLE, complex.x());
        c.set(k("y"), PersistentDataType.DOUBLE, complex.y());
        c.set(k("z"), PersistentDataType.DOUBLE, complex.z());
        return c;
    }

    @Override
    public @NotNull LocationPos fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        return new LocationPos(
            c.getOrDefault(k("x"), PersistentDataType.DOUBLE, 0D),
            c.getOrDefault(k("y"), PersistentDataType.DOUBLE, 0D),
            c.getOrDefault(k("z"), PersistentDataType.DOUBLE, 0D)
        );
    }
}