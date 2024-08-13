package killercreepr.crux.loot.conditions;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
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
        return CruxMath.random(1f, 100f) <= chance;
    }
}
