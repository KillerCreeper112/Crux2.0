package killercreepr.cruxworlds.api.world.entity;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.data.holder.DataInfoHolder;
import killercreepr.crux.api.loot.WeightedObject;
import killercreepr.cruxworlds.core.world.entity.EmptyNaturalEntitySpawn;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface NaturalEntitySpawn extends WeightedObject, DataInfoHolder {
    static NaturalEntitySpawn empty(int weight){
        return empty(weight, 0f);
    }
    static NaturalEntitySpawn empty(int weight, float quality){
        return new EmptyNaturalEntitySpawn(weight, quality);
    }

    default void appendComponentsIfNotPresent(DataComponentAccessor components){}
    default @Nullable Entity spawn(@NotNull SpawnContext ctx){
        return spawn(ctx, null);
    }
    @Nullable Entity spawn(@NotNull SpawnContext ctx, @Nullable Consumer<Entity> consumer);
    int getGroupSize(@NotNull SpawnContext ctx);
    int getGroupRadius(@NotNull SpawnContext ctx);
    boolean canSpawn(@NotNull SpawnContext ctx);
}
