package killercreepr.cruxadvancements.core.advancement.objective.standard;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.papermc.paper.event.player.PlayerTradeEvent;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.jetbrains.annotations.NotNull;

public class TradeObjective extends GenericEventObjective<PlayerTradeEvent> {
    public TradeObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerTradeEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
