package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.jetbrains.annotations.NotNull;

public class PickupItemObjective extends GenericEventObjective<EntityPickupItemEvent> {
    public PickupItemObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(EntityPickupItemEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
