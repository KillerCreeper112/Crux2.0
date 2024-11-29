package killercreepr.crux.core.location;

import killercreepr.crux.api.data.holder.LocationHolder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DynamicLocation extends Location implements LocationHolder {
    @Deprecated
    public static @NotNull EntityLocation from(@NotNull Entity e){
        return new EntityLocation(e);
    }
    public static @NotNull EntityLocation createEntity(@NotNull Entity e){
        return new EntityLocation(e);
    }
    public static @NotNull EntityLocation createEntity(@NotNull Entity e, @Nullable DynamicInfo info){
        return new EntityLocation(e, info);
    }
    @Deprecated
    public static @NotNull StaticLocation from(@NotNull Location location){
        return new StaticLocation(location, null);
    }
    public static @NotNull StaticLocation createStatic(@NotNull Location location){
        return new StaticLocation(location);
    }
    public static @NotNull StaticLocation createStatic(@NotNull Location location, @Nullable DynamicInfo info){
        return new StaticLocation(location, info);
    }

    protected final @Nullable DynamicInfo info;
    public DynamicLocation(Location loc, @Nullable DynamicInfo info) {
        super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.info = info;
    }

    public DynamicLocation(World world, double x, double y, double z, @Nullable DynamicInfo info) {
        super(world, x, y, z);
        this.info = info;
    }

    public DynamicLocation(World world, double x, double y, double z, float yaw, float pitch, @Nullable DynamicInfo info) {
        super(world, x, y, z, yaw, pitch);
        this.info = info;
    }
    public @Nullable DynamicInfo getInfo(){ return info; }
}
