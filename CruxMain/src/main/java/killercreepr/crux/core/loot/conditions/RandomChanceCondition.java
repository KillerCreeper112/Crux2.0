package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

public class RandomChanceCondition implements LootCondition {
    protected final float chance;

    public RandomChanceCondition(float chance) {
        this.chance = chance;
    }

    @Override
    public @NotNull String getTarget() {
        return "";
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        return context.getRandom().nextFloat() <= chance;
    }
}
