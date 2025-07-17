package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.event.PlayerCraftItemEvent;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.loot.context.AdvanceLootCtx;
import org.bukkit.event.inventory.SmithItemEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SmithItemObjective extends GenericEventObjective<SmithItemEvent> {
    public SmithItemObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(SmithItemEvent event) {
        return EventLootContexts.builder(event).build();
    }
}
