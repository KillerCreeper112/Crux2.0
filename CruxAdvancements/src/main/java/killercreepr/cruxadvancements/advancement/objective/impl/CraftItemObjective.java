package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.loot.LootContext;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.event.PlayerCraftItemEvent;
import killercreepr.cruxadvancements.loot.context.AdvanceLootCtx;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
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
