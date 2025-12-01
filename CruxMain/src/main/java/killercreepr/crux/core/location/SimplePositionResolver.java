package killercreepr.crux.core.location;

import killercreepr.crux.api.data.holder.LocationResolver;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;

public class SimplePositionResolver implements LocationResolver {
    protected final NumberProvider x;
    protected final NumberProvider y;
    protected final NumberProvider z;
    protected final String world;

    public SimplePositionResolver(NumberProvider x, NumberProvider y, NumberProvider z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public Location resolveLocation(InputContext ctx) {
        return new Location(
            world == null ? null : Crux.getServer().getWorld(Key.key(ctx.input(world))),
            x.sample(ctx).doubleValue(),
            y.sample(ctx).doubleValue(),
            z.sample(ctx).doubleValue()
        );
    }
}
