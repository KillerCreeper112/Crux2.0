package killercreepr.crux.api.data;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface WorldPosition extends CruxPosition {
    @NotNull Key worldKey();

    default Location toLocation(){
        World world = Crux.getServer().getWorld(worldKey());
        return toLocation(world);
    }
}
