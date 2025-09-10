package killercreepr.crux.core.world.predicate;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.world.predicate.WorldPredicate;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class WorldLootConditionPredicate implements WorldPredicate {
    protected final LootCondition condition;

    public WorldLootConditionPredicate(LootCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(@NotNull World world) {
        LootContext ctx = LootContext.builder().looted(world).build();
        return condition.test(ctx);
    }
}
