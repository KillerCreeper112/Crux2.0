package killercreepr.cruxworlds.core.world.entity;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EmptyNaturalEntitySpawn extends SimpleNaturalEntitySpawn{
    public EmptyNaturalEntitySpawn(int weight, float quality) {
        super(weight, quality);
    }

    @Override
    public @Nullable Entity spawn(@NotNull SpawnContext ctx, @Nullable Consumer<Entity> consumer) {
        return null;
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return true;
    }

    @Override
    public @NotNull DataExchange info() {
        return DataExchange.empty();
    }
}
