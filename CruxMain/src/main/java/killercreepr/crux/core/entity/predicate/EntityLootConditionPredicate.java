package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityLootConditionPredicate implements EntityPredicate {
    protected final LootCondition condition;

    public EntityLootConditionPredicate(LootCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(@NotNull Entity e) {
        LootContext ctx = LootContext.builder().looted(e).build();
        return condition.test(ctx);
    }
}
