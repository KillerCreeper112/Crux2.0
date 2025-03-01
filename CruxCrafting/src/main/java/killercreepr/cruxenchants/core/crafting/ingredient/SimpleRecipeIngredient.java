package killercreepr.cruxenchants.core.crafting.ingredient;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxenchants.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxenchants.api.crafting.ingredient.CruxRecipeIngredient;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleRecipeIngredient implements CruxRecipeIngredient {
    protected final ItemPredicate predicate;
    protected final int amount;
    //protected final Collection<ItemCrafter> crafters;
    public SimpleRecipeIngredient(ItemPredicate predicate, int amount) {
        this.predicate = predicate;
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
    public @Nullable ItemStack getItemDisplay() {
        if(predicate instanceof ItemHolder holder){
            ItemStack item = holder.value();
            item.setAmount(amount);
            return item;
        }
        return null;
    }

    @Override
    public boolean test(@NotNull CruxIngredientContext ctx) {
        ItemStack ingredient = ctx.ingredient();
        if(CruxItem.isEmpty(ingredient)) return false;
        if(ingredient.getAmount() < amount) return false;
        return predicate.test(ingredient);
    }
}
