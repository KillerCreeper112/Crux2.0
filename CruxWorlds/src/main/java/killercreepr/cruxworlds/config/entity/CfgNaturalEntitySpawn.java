package killercreepr.cruxworlds.config.entity;

import killercreepr.crux.entity.CruxEntitySnapshot;
import killercreepr.cruxworlds.world.entity.SpawnContext;
import killercreepr.cruxworlds.world.entity.impl.SimpleNaturalEntitySpawn;
import killercreepr.cruxworlds.world.spawning.SpawnValidator;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgNaturalEntitySpawn extends SimpleNaturalEntitySpawn {
    protected final @NotNull CruxEntitySnapshot entitySnapshot;
    protected final @Nullable SpawnValidator spawnValidator;
    public CfgNaturalEntitySpawn(int weight, float quality, @NotNull CruxEntitySnapshot entitySnapshot, @Nullable SpawnValidator spawnValidator) {
        super(weight, quality);
        this.entitySnapshot = entitySnapshot;
        this.spawnValidator = spawnValidator;
    }

    @Override
    public @Nullable Entity spawn(@NotNull SpawnContext ctx) {
        Location to = ctx.getPosition().toLocation(ctx.getWorld()).toCenterLocation().subtract(0, .4, 0);
        return entitySnapshot.createEntity(to);
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return spawnValidator == null || spawnValidator.canSpawn(ctx);
    }
}
