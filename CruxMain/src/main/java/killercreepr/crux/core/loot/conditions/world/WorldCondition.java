package killercreepr.crux.core.loot.conditions.world;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldCondition extends BaseCondition {
    protected final @Nullable String name;
    protected final @Nullable String dimension;

    public WorldCondition(@NotNull String target, @Nullable String name, @Nullable String dimension) {
        super(target);
        this.name = name;
        this.dimension = dimension;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        World b = ctx.info().get(target, World.class);
        if(b==null) return false;
        if(name != null && !b.getName().equals(name)) return false;
        if(dimension != null && !b.getEnvironment().toString().equalsIgnoreCase(dimension)) return false;
        return true;
    }
}
