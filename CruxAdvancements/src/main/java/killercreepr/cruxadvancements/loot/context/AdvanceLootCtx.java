package killercreepr.cruxadvancements.loot.context;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.LootContext;
import killercreepr.cruxadvancements.event.PlayerCraftItemEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class AdvanceLootCtx {
    public static LootContext.Builder builder(PlayerCraftItemEvent event){
        HumanEntity p = event.getWhoClicked();
        ItemStack item = event.getItemResult();
        return LootContext.builder()
            .info(
                DataExchange.builder()
                    .putAll(p, "player")
                    .putAll(item, "item", "crafted", "result")
                    .putAll(event.getResultAmount(), "result_amount", "amount")
                    .build()
            )
            .location(p.getLocation())
            .looter(p)
            .looted(item)
            ;
    }
}
