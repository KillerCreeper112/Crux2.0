package killercreepr.cruxadvancements.core.loot.context;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.event.CruxAdvancementGrantEvent;
import killercreepr.cruxadvancements.api.event.PlayerCraftItemEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

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
    public static LootContext.Builder builder(CruxAdvancementGrantEvent event){
        UUID uuid = event.getWho();
        Player p = Crux.getServer().getPlayer(uuid);
        return LootContext.builder()
            .info(
                DataExchange.builder()
                    .putAll(p, "player")
                    .putAll(event.getManager(), "manager")
                    .build()
            )
            .looter(p)
            .looted(event.getAdvancement())
            ;
    }
}
