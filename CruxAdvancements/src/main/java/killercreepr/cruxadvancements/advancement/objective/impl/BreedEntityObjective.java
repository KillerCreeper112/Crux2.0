package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.entity.EntityBreedEvent;
import org.jetbrains.annotations.NotNull;

public class BreedEntityObjective extends GenericEventObjective<EntityBreedEvent> {
    public BreedEntityObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(EntityBreedEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
