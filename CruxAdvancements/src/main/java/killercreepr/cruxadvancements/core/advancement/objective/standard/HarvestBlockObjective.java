package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.jetbrains.annotations.NotNull;

public class HarvestBlockObjective extends GenericEventObjective<PlayerHarvestBlockEvent> {
    public HarvestBlockObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerHarvestBlockEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
