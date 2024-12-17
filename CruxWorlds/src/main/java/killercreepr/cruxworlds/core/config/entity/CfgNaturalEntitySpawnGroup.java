package killercreepr.cruxworlds.core.config.entity;

import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import killercreepr.cruxworlds.core.world.entity.SimpleNaturalEntitySpawnGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CfgNaturalEntitySpawnGroup extends SimpleNaturalEntitySpawnGroup {
    protected final @Nullable SpawnValidator spawnValidator;
    public CfgNaturalEntitySpawnGroup(int weight, float quality, @NotNull Collection<NaturalEntitySpawn> spawns, @Nullable SpawnValidator spawnValidator) {
        super(weight, quality, spawns);
        this.spawnValidator = spawnValidator;
    }

    public CfgNaturalEntitySpawnGroup(int weight, float quality, @Nullable SpawnValidator spawnValidator, @NotNull NaturalEntitySpawn... spawns) {
        super(weight, quality, spawns);
        this.spawnValidator = spawnValidator;
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return spawnValidator == null || spawnValidator.canSpawn(ctx);
    }
}
