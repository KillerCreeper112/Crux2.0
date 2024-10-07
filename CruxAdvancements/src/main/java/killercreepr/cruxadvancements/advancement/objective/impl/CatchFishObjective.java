package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.loot.LootContext;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CatchFishObjective extends NumberObjective {
    public CatchFishObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull PlayerFishEvent event){
        LootContext ctx = LootContext.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
