package killercreepr.crux.core.persistence.type;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.util.CruxEntityUtil;
import killercreepr.crux.core.util.CruxTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationTagType implements PersistentDataType<PersistentDataContainer, Location> {
    public static NamespacedKey k(@NotNull String name){ return Crux.key(name); }
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Location> getComplexType() {
        return Location.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Location complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        if(complex.getWorld() != null) c.set(k("world"), CruxPersistence.UUID, complex.getWorld().getUID());
        c.set(k("x"), PersistentDataType.DOUBLE, complex.getX());
        c.set(k("y"), PersistentDataType.DOUBLE, complex.getY());
        c.set(k("z"), PersistentDataType.DOUBLE, complex.getZ());
        c.set(k("yaw"), PersistentDataType.FLOAT, complex.getYaw());
        CruxEntityUtil
        c.set(k("pitch"), PersistentDataType.FLOAT, complex.getPitch());
        return c;
    }

    @Override
    public @NotNull Location fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        World w = null;
        UUID worldUUID = CruxTag.get(c, k("world"), CruxPersistence.UUID, null);
        if(worldUUID != null) w = Bukkit.getWorld(worldUUID);
        return new Location(w, c.getOrDefault(k("x"), PersistentDataType.DOUBLE, 0D),
                c.getOrDefault(k("y"), PersistentDataType.DOUBLE, 0D),
                c.getOrDefault(k("z"), PersistentDataType.DOUBLE, 0D),
                c.getOrDefault(k("yaw"), PersistentDataType.FLOAT, 0f),
                c.getOrDefault(k("pitch"), PersistentDataType.FLOAT, 0f));
    }
}