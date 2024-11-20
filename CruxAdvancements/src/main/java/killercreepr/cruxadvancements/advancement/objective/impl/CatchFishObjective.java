package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;

public class CatchFishObjective extends GenericEventObjective<PlayerFishEvent> {
    public CatchFishObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerFishEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
