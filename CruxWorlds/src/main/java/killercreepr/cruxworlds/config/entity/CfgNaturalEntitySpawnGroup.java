package killercreepr.cruxworlds.config.entity;

import killercreepr.cruxworlds.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.world.entity.SpawnContext;
import killercreepr.cruxworlds.world.entity.impl.SimpleNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.spawning.SpawnValidator;
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
