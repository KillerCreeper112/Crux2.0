package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.block.BlockDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class BreakBlockDropObjective extends GenericEventObjective<BlockDropItemEvent> {
    public BreakBlockDropObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(BlockDropItemEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
