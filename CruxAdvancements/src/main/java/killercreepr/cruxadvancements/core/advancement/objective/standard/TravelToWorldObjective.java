package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
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
