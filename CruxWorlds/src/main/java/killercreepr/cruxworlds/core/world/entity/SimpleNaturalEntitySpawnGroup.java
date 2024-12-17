package killercreepr.cruxworlds.core.world.entity;

import killercreepr.crux.api.util.CruxWeightedSupplier;
import killercreepr.crux.core.loot.SimpleWeighted;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public abstract class SimpleNaturalEntitySpawnGroup extends SimpleWeighted implements NaturalEntitySpawnGroup {
    protected final @NotNull Collection<NaturalEntitySpawn> spawns;
    public SimpleNaturalEntitySpawnGroup(int weight, float quality, @NotNull Collection<NaturalEntitySpawn> spawns) {
        super(weight, quality);
        this.spawns = spawns;
    }

    public SimpleNaturalEntitySpawnGroup(int weight, float quality, @NotNull NaturalEntitySpawn... spawns) {
        this(weight, quality, Set.of(spawns));
    }

    @NotNull
    @Override
    public Collection<NaturalEntitySpawn> selectRandom(int rolls, @NotNull SpawnContext ctx) {
        return CruxWeightedSupplier.builder(spawns)
            .rolls(rolls)
            .filter(check -> check.canSpawn(ctx))
            .build().rollList();
    }

    @NotNull
    @Override
    public Collection<NaturalEntitySpawn> getAllAvailableSpawns() {
        return spawns;
    }
}
