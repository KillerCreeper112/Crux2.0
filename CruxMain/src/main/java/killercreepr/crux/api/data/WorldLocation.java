package killercreepr.crux.api.data;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.core.data.SimpleWorldLocation;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;

public interface WorldLocation extends WorldPosition, CruxLocation {
    static WorldLocation worldLocation(double x, double y, double z, float yaw, float pitch, Key world){
        return new SimpleWorldLocation(x, y, z, yaw, pitch, world);
    }

    static WorldLocation worldLocation(double x, double y, double z, Key world){
        return worldLocation(x, y, z, 0f, 0f, world);
    }

    @ApiStatus.Experimental
    default Key worldKeyOrDefault(Key fallback){
        Key key = worldKey();
        return key == null ? fallback : key;
    }
}
