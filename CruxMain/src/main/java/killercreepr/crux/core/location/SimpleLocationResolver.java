package killercreepr.crux.core.location;

import killercreepr.crux.api.data.holder.LocationResolver;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;

public class SimpleLocationResolver implements LocationResolver {
  protected final NumberProvider x;
  protected final NumberProvider y;
  protected final NumberProvider z;
  protected final NumberProvider yaw;
  protected final NumberProvider pitch;
  protected final String world;

  public SimpleLocationResolver(NumberProvider x, NumberProvider y, NumberProvider z, NumberProvider yaw, NumberProvider pitch, String world) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
    this.world = world;
  }

  public NumberProvider getX() {
    return x;
  }

  public NumberProvider getY() {
    return y;
  }

  public NumberProvider getZ() {
    return z;
  }

  public NumberProvider getYaw() {
    return yaw;
  }

  public NumberProvider getPitch() {
    return pitch;
  }

  public String getWorld() {
    return world;
  }

  @Override
  public Location resolveLocation(InputContext ctx) {
    return new Location(
      world == null ? null : Crux.getServer().getWorld(Key.key(ctx.input(world))),
      x.sample(ctx).doubleValue(),
      y.sample(ctx).doubleValue(),
      z.sample(ctx).doubleValue(),
      yaw.sample(ctx).floatValue(),
      pitch.sample(ctx).floatValue()
    );
  }
}
