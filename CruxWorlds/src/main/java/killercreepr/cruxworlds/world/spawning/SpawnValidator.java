package killercreepr.cruxworlds.world.spawning;

import killercreepr.cruxworlds.world.entity.SpawnContext;
import org.jetbrains.annotations.NotNull;

public interface SpawnValidator {
    boolean canSpawn(@NotNull SpawnContext ctx);
}
