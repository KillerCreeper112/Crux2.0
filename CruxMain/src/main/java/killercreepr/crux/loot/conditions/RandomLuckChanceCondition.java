package killercreepr.crux.loot.conditions;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.util.CruxMath;
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
        return CruxMath.random(1f, 100f) <= c;
    }
}
