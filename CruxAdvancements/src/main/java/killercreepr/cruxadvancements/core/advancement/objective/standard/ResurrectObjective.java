package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.jetbrains.annotations.NotNull;

public class ResurrectObjective extends GenericEventObjective<EntityResurrectEvent> {
    public ResurrectObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(EntityResurrectEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
