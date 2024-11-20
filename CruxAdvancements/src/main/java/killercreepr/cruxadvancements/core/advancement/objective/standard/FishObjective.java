package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FishObjective extends NumberObjective {
    public FishObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull PlayerFishEvent event){
        LootContext ctx = EventLootContexts.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
