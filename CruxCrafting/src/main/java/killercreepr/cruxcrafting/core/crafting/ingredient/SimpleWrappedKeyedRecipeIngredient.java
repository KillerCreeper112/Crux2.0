package killercreepr.cruxcrafting.core.crafting.ingredient;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleWrappedKeyedRecipeIngredient extends SimpleWrappedRecipeIngredient implements CruxKeyed {
    protected final Key key;
    public SimpleWrappedKeyedRecipeIngredient(CruxRecipeIngredient ingredient, int amount, Key key) {
        super(ingredient, amount);
        this.key = key;
    }

    /**
     * Gets the key.
     *
     * @return the key
     * @since 4.0.0
     */
    @Override
    public @NotNull Key key() {
        return key;
    }
}
