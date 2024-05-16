package killercreepr.crux.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public abstract class DynamicLocation extends Location implements LocationHolder {
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
