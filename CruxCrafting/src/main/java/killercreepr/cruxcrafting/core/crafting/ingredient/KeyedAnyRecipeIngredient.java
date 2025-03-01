package killercreepr.cruxcrafting.core.crafting.ingredient;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class KeyedAnyRecipeIngredient extends AnyRecipeIngredient implements CruxKeyed {
    protected final Key key;

    public KeyedAnyRecipeIngredient(Collection<CruxRecipeIngredient> terms, List<ItemStack> displays, Key key) {
        super(terms, displays);
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
