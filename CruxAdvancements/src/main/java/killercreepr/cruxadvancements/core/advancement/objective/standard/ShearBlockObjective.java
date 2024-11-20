package killercreepr.cruxadvancements.core.advancement.objective.standard;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
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
