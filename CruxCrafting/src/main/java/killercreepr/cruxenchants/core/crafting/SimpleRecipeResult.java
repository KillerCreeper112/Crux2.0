package killercreepr.cruxenchants.core.crafting;

import killercreepr.cruxenchants.api.crafting.CruxRecipeResult;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleRecipeResult implements CruxRecipeResult {
    protected final List<ItemStack> resultItems;

    public SimpleRecipeResult(List<ItemStack> resultItems) {
        this.resultItems = resultItems;
    }

    public SimpleRecipeResult(ItemStack result) {
        this(List.of(result));
    }

    @Override
    public @NotNull List<ItemStack> getResultItems() {
        return resultItems;
    }
}
