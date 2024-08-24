package killercreepr.cruxworlds.world.entity.entity;

import killercreepr.crux.loot.WeightedObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface NaturalEntitySpawnGroup extends WeightedObject {
    boolean canSpawn(@NotNull SpawnContext ctx);
    @NotNull Collection<NaturalEntitySpawn> selectRandom(int rolls, @NotNull SpawnContext ctx);
    @NotNull Collection<NaturalEntitySpawn> getAllAvailableSpawns();
}
