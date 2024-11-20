package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BreakBlockObjective extends GenericEventObjective<BlockBreakEvent> {
    public BreakBlockObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(BlockBreakEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
