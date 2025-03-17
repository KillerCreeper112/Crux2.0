package killercreepr.cruxcrafting.core.loot.condition;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public class CruxRecipeCondition extends BaseCondition {
    protected final Key key;
    public CruxRecipeCondition(@NotNull String target, Key key) {
        super(target);
        this.key = key;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        CruxRecipe recipe = ctx.info().get(target, CruxRecipe.class);
        if(recipe == null) return false;
        if(key != null && (!(recipe instanceof Keyed k) || !k.key().equals(key))) return false;
        return true;
    }
}
