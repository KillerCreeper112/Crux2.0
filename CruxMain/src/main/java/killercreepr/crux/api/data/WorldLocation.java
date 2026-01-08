package killercreepr.crux.api.data;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.SimpleWorldLocation;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;

public interface WorldLocation {
    static WorldLocation worldLocation(double x, double y, double z, float yaw, float pitch, Key world){
        return new SimpleWorldLocation(world, CruxLocation.location(x, y, z, yaw, pitch));
    }

    static WorldLocation worldLocation(double x, double y, double z, Key world){
        return worldLocation(x, y, z, 0f, 0f, world);
    }

    default Location toLocation(){
        return toLocation(worldKey() == null ? null : Crux.getServer().getWorld(worldKey()));
    }

    default Location toLocation(World world){
        return new Location(
            world,
            location().x(), location().y(), location().z(),
            location().yaw(), location().pitch()
        );
    }

    Key worldKey();
    CruxLocation location();

    @ApiStatus.Experimental
    default Key worldKeyOrDefault(Key fallback){
        Key key = worldKey();
        return key == null ? fallback : key;
    }
}
