package killercreepr.cruxcrafting.api.crafting.ingredient;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CruxRecipeIngredient {
    static CruxRecipeIngredient empty(){
        return EMPTY;
    }
    CruxRecipeIngredient EMPTY = new Empty();

    /**
     * Called when the crafting menu is removing the
     * item mapped to this ingredient.
     */
    ItemStack removeItem(@NotNull CruxIngredientContext ctx);

    /**
     * Tests if this ingredient matches the context.
     */
    boolean test(@NotNull CruxIngredientContext ctx);

    /**
     * Used only when displaying what ingredients a recipe
     * has.
     */
    @Nullable List<ItemStack> getItemDisplay();

    class Empty implements CruxRecipeIngredient {

        @Override
        public ItemStack removeItem(@NotNull CruxIngredientContext ctx) {
            return ctx.ingredient();
        }

        @Override
        public boolean test(@NotNull CruxIngredientContext ctx) {
            return CruxItem.isEmpty(ctx.ingredient());
        }

        @Override
        public @Nullable List<ItemStack> getItemDisplay() {
            return null;
        }
    }
}
