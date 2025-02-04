package killercreepr.cruxworlds.core.config.entity;

import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
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
    protected final Boolean persistent;
    protected final Boolean removeWhenFarAway;
    public CfgNaturalEntitySpawn(int weight, float quality, @NotNull CruxEntitySnapshot entitySnapshot, @Nullable SpawnValidator spawnValidator,
                                 Boolean persistent, Boolean removeWhenFarAway) {
        super(weight, quality);
        this.entitySnapshot = entitySnapshot;
        this.spawnValidator = spawnValidator;
        this.persistent = persistent;
        this.removeWhenFarAway = removeWhenFarAway;
    }

    @Override
    public @Nullable Entity spawn(@NotNull SpawnContext ctx, @Nullable Consumer<Entity> consumer) {
        Location to = ctx.getPosition().toLocation(ctx.getWorld()).toCenterLocation().subtract(0, .4, 0);
        return entitySnapshot.createEntity(to, e ->{
            if(persistent != null) e.setPersistent(persistent);
            if(removeWhenFarAway != null){
                if(e instanceof Mob m) m.setRemoveWhenFarAway(removeWhenFarAway);
            }
            if(consumer != null) consumer.accept(e);
        });
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return spawnValidator == null || spawnValidator.canSpawn(ctx);
    }
}
