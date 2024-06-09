package killercreepr.crux.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StaticLocation extends DynamicLocation{
    public StaticLocation(@NotNull Block block, @Nullable DynamicInfo info) {
        super(block.getLocation(), info);
    }

    public StaticLocation(@NotNull Entity entity, @Nullable DynamicInfo info) {
        super(entity.getLocation(), info);
    }

    public StaticLocation(Location loc){
        this(loc, null);
    }
    public StaticLocation(Location loc, @Nullable DynamicInfo info) {
        super(loc, info);
    }

    public StaticLocation(World world, double x, double y, double z, @Nullable DynamicInfo info) {
        super(world, x, y, z, info);
    }

    public StaticLocation(World world, double x, double y, double z, float yaw, float pitch, @Nullable DynamicInfo info) {
        super(world, x, y, z, yaw, pitch, info);
    }

    @Override
    public @NotNull Location value() {
        return info == null ? this : info.apply(this.clone());
    }
}
