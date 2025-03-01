package killercreepr.cruxcrafting.core.crafting.ingredient;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class KeyedAnyRecipeIngredient extends AnyRecipeIngredient implements CruxKeyed {
    protected final Key key;
    public KeyedAnyRecipeIngredient(Collection<CruxRecipeIngredient> terms, Key key) {
        super(terms);
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
