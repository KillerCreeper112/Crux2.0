package killercreepr.cruxworlds.world.entity;

import killercreepr.crux.loot.WeightedObject;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NaturalEntitySpawn extends WeightedObject {
    @Nullable Entity spawn(@NotNull SpawnContext ctx);
    int getGroupSize(@NotNull SpawnContext ctx);
    int getGroupRadius(@NotNull SpawnContext ctx);
    boolean canSpawn(@NotNull SpawnContext ctx);
}
