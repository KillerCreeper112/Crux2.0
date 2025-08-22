package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.inventory.SmithItemEvent;
import org.jetbrains.annotations.NotNull;

public class SmithItemObjective extends GenericEventObjective<SmithItemEvent> {
    public SmithItemObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(SmithItemEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
