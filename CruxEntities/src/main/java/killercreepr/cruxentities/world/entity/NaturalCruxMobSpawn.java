package killercreepr.cruxentities.world.entity;

import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxworlds.world.entity.NaturalEntityGroupPart;
import killercreepr.cruxworlds.world.entity.SpawnContext;
import killercreepr.cruxworlds.world.entity.impl.SimpleNaturalEntitySpawn;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NaturalCruxMobSpawn extends SimpleNaturalEntitySpawn implements NaturalEntityGroupPart {
    protected final @NotNull CruxMob mob;
    public NaturalCruxMobSpawn(int weight, float quality, @NotNull CruxMob mob) {
        super(weight, quality);
        this.mob = mob;
    }

    @Override
    public @Nullable Entity spawn(@NotNull SpawnContext ctx) {
        return mob.spawn(ctx.getBlock().getLocation().toCenterLocation().subtract(0, .5, 0));
    }

    @Override
    public boolean isPartOfGroup(@NotNull Entity check){
        return CruxMob.is(check, mob);
    }

    public @NotNull CruxMob getMob() {
        return mob;
    }
}
