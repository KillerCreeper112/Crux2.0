package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ShearEntityObjective extends GenericEventObjective<PlayerShearEntityEvent> {
    public ShearEntityObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerShearEntityEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
