package killercreepr.crux.core.data;

import killercreepr.crux.api.data.WorldPosition;
import killercreepr.crux.core.math.LocationPos;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleWorldPosition extends LocationPos implements WorldPosition {
    protected final Key world;
    public SimpleWorldPosition(double x, double y, double z, Key world) {
        super(x, y, z);
        this.world = world;
    }

    @Override
    public @NotNull Key worldKey() {
        return world;
    }
}
