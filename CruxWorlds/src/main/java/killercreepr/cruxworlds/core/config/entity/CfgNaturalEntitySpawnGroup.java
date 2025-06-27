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
    protected final int rolls;
    public CfgNaturalEntitySpawnGroup(int weight, float quality, @NotNull Collection<NaturalEntitySpawn> spawns, @Nullable SpawnValidator spawnValidator, int rolls) {
        super(weight, quality, spawns);
        this.spawnValidator = spawnValidator;
        this.rolls = rolls;
    }

    public CfgNaturalEntitySpawnGroup(int weight, float quality, @Nullable SpawnValidator spawnValidator, int rolls, @NotNull NaturalEntitySpawn... spawns) {
        super(weight, quality, spawns);
        this.spawnValidator = spawnValidator;
        this.rolls = rolls;
    }

    @Override
    public @NotNull Collection<NaturalEntitySpawn> selectRandom(@NotNull SpawnContext ctx) {
        return selectRandom(rolls, ctx);
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return spawnValidator == null || spawnValidator.canSpawn(ctx);
    }
}
