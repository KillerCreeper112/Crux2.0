package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TravelToWorldObjective extends NumberObjective {
    public TravelToWorldObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                        @NotNull CruxAdvancementManager manager,
                        @NotNull ObjectiveAdvancement advancement,
                        @NotNull PlayerChangedWorldEvent event){
        LootContext ctx = EventLootContexts.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
