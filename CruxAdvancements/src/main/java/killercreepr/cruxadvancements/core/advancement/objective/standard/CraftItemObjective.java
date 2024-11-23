package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.event.PlayerCraftItemEvent;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.loot.context.AdvanceLootCtx;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CraftItemObjective extends GenericEventObjective<PlayerCraftItemEvent> {
    public CraftItemObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerCraftItemEvent event) {
        return AdvanceLootCtx.builder(event).build();
    }

    @Override
    public boolean trigger(@NotNull UUID who, @NotNull CruxAdvancementManager manager, @NotNull ObjectiveAdvancement advancement, @NotNull PlayerCraftItemEvent event) {
        return trigger(who, manager, advancement, event, event.getResultAmount());
    }
}
