package killercreepr.cruxcrafting.core.crafting.ingredient;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleKeyedRecipeIngredient extends SimpleRecipeIngredient implements CruxKeyed {
    protected final Key key;
    public SimpleKeyedRecipeIngredient(ItemPredicate predicate, int amount, List<ItemStack> itemDisplays, Key key) {
        super(predicate, amount, itemDisplays);
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
