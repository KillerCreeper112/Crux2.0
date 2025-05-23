package killercreepr.cruxadvancements.core.advancement.objective.standard;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.jetbrains.annotations.NotNull;

public class ArmorChangeObjective extends GenericEventObjective<PlayerArmorChangeEvent> {
    public ArmorChangeObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerArmorChangeEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
