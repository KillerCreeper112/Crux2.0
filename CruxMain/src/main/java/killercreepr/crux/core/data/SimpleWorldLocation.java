package killercreepr.crux.core.data;

import killercreepr.crux.api.data.WorldLocation;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.core.math.SimpleCruxLocation;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleWorldLocation implements WorldLocation {
    protected final Key world;
    protected final CruxLocation location;

    public SimpleWorldLocation(Key world, CruxLocation location) {
        this.world = world;
        this.location = location;
    }


    @Override
    public @NotNull Key worldKey() {
        return world;
    }

    @Override
    public CruxLocation location() {
        return location;
    }
}
