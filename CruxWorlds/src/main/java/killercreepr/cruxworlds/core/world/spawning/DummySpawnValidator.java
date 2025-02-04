package killercreepr.cruxworlds.core.world.spawning;

import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import org.jetbrains.annotations.NotNull;

public class DummySpawnValidator implements SpawnValidator {
    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return true;
    }
}
