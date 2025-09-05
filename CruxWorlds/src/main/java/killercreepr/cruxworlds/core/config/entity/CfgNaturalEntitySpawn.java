package killercreepr.cruxworlds.core.config.entity;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import killercreepr.cruxworlds.core.component.EntitySpawnPassengers;
import killercreepr.cruxworlds.core.world.entity.SimpleNaturalEntitySpawn;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CfgNaturalEntitySpawn extends SimpleNaturalEntitySpawn {
    protected final @NotNull CruxEntitySnapshot entitySnapshot;
    protected final @Nullable SpawnValidator spawnValidator;
    protected final DataExchange info;
    protected final DataComponentAccessor components;
    public CfgNaturalEntitySpawn(int weight, float quality, @NotNull CruxEntitySnapshot entitySnapshot, @Nullable SpawnValidator spawnValidator,
                                 DataExchange info, DataComponentAccessor components) {
        super(weight, quality);
        this.entitySnapshot = entitySnapshot;
        this.spawnValidator = spawnValidator;
        this.info = info;
        this.components = components;
    }

    public @NotNull CruxEntitySnapshot getEntitySnapshot() {
        return entitySnapshot;
    }

    public @Nullable SpawnValidator getSpawnValidator() {
        return spawnValidator;
    }

    public DataExchange getInfo() {
        return info;
    }

    public DataComponentAccessor getComponents() {
        return components;
    }

    @Override
    public @Nullable Entity spawn(@NotNull SpawnContext ctx, @Nullable Consumer<Entity> consumer) {
        Location to = ctx.getPosition().toLocation(ctx.getWorld()).toCenterLocation().subtract(0, .4, 0);

        return entitySnapshot.createEntity(to, e ->{
            if(info.has("persistent")) e.setPersistent(info.getOrDefault("persistent", Boolean.class, false));
            if(info.has("remove_when_far_away") && e instanceof Mob m){
                m.setRemoveWhenFarAway(info.getOrDefault("remove_when_far_away", Boolean.class, false));
            }

            EntitySpawnPassengers passengers = components.get(CruxWorldsComponents.ENTITY_SPAWN_PASSENGERS);
            if(passengers != null){
                passengers.passengers.forEach(spawn ->{
                    Entity spawned = spawn.spawn(ctx);
                    if(spawned == null) return;
                    e.addPassenger(spawned);
                });
            }

            if(consumer != null) consumer.accept(e);
        });
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return spawnValidator == null || spawnValidator.canSpawn(ctx);
    }

    @Override
    public @NotNull DataExchange info() {
        return info;
    }
}
