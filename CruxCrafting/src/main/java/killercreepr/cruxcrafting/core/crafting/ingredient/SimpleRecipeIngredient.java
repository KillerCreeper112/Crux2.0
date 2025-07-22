package killercreepr.cruxcrafting.core.crafting.ingredient;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleRecipeIngredient implements CruxRecipeIngredient {
    protected final ItemPredicate predicate;
    protected final int amount;
    protected final List<ItemStack> itemDisplays;
    //protected final Collection<ItemCrafter> crafters;
    public SimpleRecipeIngredient(ItemPredicate predicate, int amount, List<ItemStack> itemDisplays) {
        this.predicate = predicate;
        this.amount = amount;
        this.itemDisplays = itemDisplays;
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
        return itemDisplays;
    }

    @Override
    public boolean test(@NotNull CruxIngredientContext ctx) {
        ItemStack ingredient = ctx.ingredient();
        if(CruxItem.isEmpty(ingredient)) return false;
        if(ingredient.getAmount() < amount) return false;
        return predicate.test(ingredient);
    }

    public ItemPredicate getPredicate() {
        return predicate;
    }

    public int getAmount() {
        return amount;
    }

    public List<ItemStack> getItemDisplays() {
        return itemDisplays;
    }
}
