package killercreepr.cruxworlds.api.world.entity;

import killercreepr.crux.api.loot.WeightedObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface NaturalEntitySpawnGroup extends WeightedObject {
    boolean canSpawn(@NotNull SpawnContext ctx);
    @NotNull Collection<NaturalEntitySpawn> selectRandom(@NotNull SpawnContext ctx);
    @NotNull Collection<NaturalEntitySpawn> selectRandom(int rolls, @NotNull SpawnContext ctx);
    @NotNull Collection<NaturalEntitySpawn> getAllAvailableSpawns();
}
