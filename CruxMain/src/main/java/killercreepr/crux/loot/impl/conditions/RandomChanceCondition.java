package killercreepr.crux.loot.impl.conditions;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.util.CruxMath;
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
