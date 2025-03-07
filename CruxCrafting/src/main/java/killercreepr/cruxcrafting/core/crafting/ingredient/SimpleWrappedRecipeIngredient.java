package killercreepr.cruxcrafting.core.crafting.ingredient;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleWrappedRecipeIngredient implements CruxRecipeIngredient {
    protected final CruxRecipeIngredient ingredient;
    protected final int amount;

    public SimpleWrappedRecipeIngredient(CruxRecipeIngredient ingredient, int amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    @Override
    public ItemStack removeItem(@NotNull CruxIngredientContext ctx) {
        ItemStack item = ctx.ingredient();
        if(item==null) return null;
        if(amount < 1) return item;
        item.setAmount(item.getAmount()-amount);
        return item;
    }

    @Override
    public @Nullable List<ItemStack> getItemDisplay() {
        var display = ingredient.getItemDisplay();
        if(display == null) return null;
        List<ItemStack> list = new ArrayList<>();
        for(ItemStack i : display){
            i = i.clone();
            i.setAmount(amount);
            list.add(i);
        }
        return list;
    }

    @Override
    public boolean test(@NotNull CruxIngredientContext ctx) {
        if(!ingredient.test(ctx)) return false;
        ItemStack ingredient = ctx.ingredient();
        if(CruxItem.isEmpty(ingredient)) return false;
        return ingredient.getAmount() >= amount;
    }
}
