package killercreepr.cruxworlds.world.entity.entity.impl;

import killercreepr.crux.loot.impl.SimpleWeighted;
import killercreepr.crux.util.CruxWeightedSupplier;
import killercreepr.cruxworlds.world.entity.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.world.entity.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.entity.entity.SpawnContext;
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
