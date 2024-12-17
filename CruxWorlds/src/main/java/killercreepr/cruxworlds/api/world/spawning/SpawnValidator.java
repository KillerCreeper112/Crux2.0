package killercreepr.cruxworlds.api.world.spawning;

import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.jetbrains.annotations.NotNull;

public interface SpawnValidator {
    boolean canSpawn(@NotNull SpawnContext ctx);
}
