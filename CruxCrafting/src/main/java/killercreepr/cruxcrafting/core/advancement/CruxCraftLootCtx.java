package killercreepr.cruxcrafting.core.advancement;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxcrafting.api.crafting.crafter.CruxCraftingCrafter;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import killercreepr.cruxcrafting.api.event.PlayerCruxCraftEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CruxCraftLootCtx {
    public static LootContext.Builder builder(PlayerCruxCraftEvent event){
        HumanEntity p = event.getPlayer();
        CruxRecipe recipe = event.getRecipe();
        List<ItemStack> results = event.getResults();
        CruxCraftingCrafter crafter = event.getCrafter();
        return LootContext.builder()
            .info(
                DataExchange.builder()
                    .putAll(p, "player")
                    .putAll(results, "item", "crafted", "result")
                    .putAll(crafter, "crafter")
                    .build()
            )
            .location(p.getLocation())
            .looter(p)
            .looted(recipe)
            ;
    }
}
