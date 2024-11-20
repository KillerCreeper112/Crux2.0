package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.entity.EntityTameEvent;
import org.jetbrains.annotations.NotNull;

public class TameEntityObjective extends GenericEventObjective<EntityTameEvent> {
    public TameEntityObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(EntityTameEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
