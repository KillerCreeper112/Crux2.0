package killercreepr.cruxentities.world.entity;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxworlds.api.world.entity.NaturalEntityGroupPart;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.core.world.entity.SimpleNaturalEntitySpawn;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class NaturalCruxMobSpawn extends SimpleNaturalEntitySpawn implements NaturalEntityGroupPart {
    protected final @NotNull CruxMob mob;
    public NaturalCruxMobSpawn(int weight, float quality, @NotNull CruxMob mob) {
        super(weight, quality);
        this.mob = mob;
    }

    @Override
    public @NotNull DataExchange info() {
        return DataExchange.empty();
    }

    @Override
    public @Nullable Entity spawn(@NotNull SpawnContext ctx, @Nullable Consumer<Entity> consumer) {
        return mob.spawn(ctx.getBlock().getLocation().toCenterLocation().subtract(0, .5, 0), consumer);
    }

    @Override
    public boolean isPartOfGroup(@NotNull Entity check){
        return CruxMob.is(check, mob);
    }

    public @NotNull CruxMob getMob() {
        return mob;
    }
}
