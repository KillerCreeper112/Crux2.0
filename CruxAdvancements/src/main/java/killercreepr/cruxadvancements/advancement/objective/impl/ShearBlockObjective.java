package killercreepr.cruxadvancements.advancement.objective.impl;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import org.jetbrains.annotations.NotNull;

public class ShearBlockObjective extends GenericEventObjective<PlayerShearBlockEvent> {
    public ShearBlockObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerShearBlockEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
