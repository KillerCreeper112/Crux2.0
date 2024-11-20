package killercreepr.cruxworlds.config.entity;

import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.cruxworlds.world.entity.SpawnContext;
import killercreepr.cruxworlds.world.entity.impl.SimpleNaturalEntitySpawn;
import killercreepr.cruxworlds.world.spawning.SpawnValidator;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @Nullable Entity spawn(@NotNull SpawnContext ctx) {
        Location to = ctx.getPosition().toLocation(ctx.getWorld()).toCenterLocation().subtract(0, .4, 0);
        return entitySnapshot.createEntity(to, e ->{
            if(persistent != null) e.setPersistent(persistent);
            if(removeWhenFarAway != null){
                if(e instanceof Mob m) m.setRemoveWhenFarAway(removeWhenFarAway);
            }
        });
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return spawnValidator == null || spawnValidator.canSpawn(ctx);
    }
}
