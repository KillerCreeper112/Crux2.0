package killercreepr.crux.loot.impl.conditions;

import killercreepr.crux.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class RandomLuckChanceCondition extends RandomChanceCondition {
    private final float luckMultiplier;

    public RandomLuckChanceCondition(float chance, float luckMultiplier) {
        super(chance);
        this.luckMultiplier = luckMultiplier;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        float c = (chance + (context.getLuck() * luckMultiplier));
        return context.getRandom().nextFloat() <= c;
    }
}
