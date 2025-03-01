package killercreepr.cruxcrafting.core.crafting.ingredient;

import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnyRecipeIngredient implements CruxRecipeIngredient {
    protected final Collection<CruxRecipeIngredient> terms;

    public AnyRecipeIngredient(Collection<CruxRecipeIngredient> terms) {
        this.terms = terms;
    }

    @Override
    public ItemStack removeItem(@NotNull CruxIngredientContext ctx) {
        for(CruxRecipeIngredient in : terms){
            if(in.test(ctx)) return in.removeItem(ctx);
        }
        return ctx.ingredient();
    }


    @Override
    public boolean test(@NotNull CruxIngredientContext ctx) {
        for(CruxRecipeIngredient in : terms){
            if(in.test(ctx)) return true;
        }
        return false;
    }


    @Override
    public @Nullable List<ItemStack> getItemDisplay() {
        List<ItemStack> list = new ArrayList<>();
        for(CruxRecipeIngredient in : terms){
            List<ItemStack> got = in.getItemDisplay();
            if(got==null) continue;
            list.addAll(got);
        }
        return list;
    }
}
