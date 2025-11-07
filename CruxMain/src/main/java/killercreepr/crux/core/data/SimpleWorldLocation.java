package killercreepr.crux.core.data;

import killercreepr.crux.api.data.WorldLocation;
import killercreepr.crux.core.math.SimpleCruxLocation;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleWorldLocation extends SimpleCruxLocation implements WorldLocation {
    protected final Key world;
    public SimpleWorldLocation(double x, double y, double z, float yaw, float pitch, Key world) {
        super(x, y, z, yaw, pitch);
        this.world = world;
    }

    @Override
    public @NotNull Key worldKey() {
        return world;
    }
}
