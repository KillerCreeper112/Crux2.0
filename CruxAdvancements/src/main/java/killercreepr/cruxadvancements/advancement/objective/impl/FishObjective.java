package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FishObjective extends NumberObjective {
    public FishObjective(@NotNull String criterion, @Nullable LootCondition conditions, int maxProgress) {
        super(criterion, conditions, maxProgress);
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
