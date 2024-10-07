package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlaceBlockObjective extends NumberObjective {
    public PlaceBlockObjective(@NotNull String criterion, @Nullable LootCondition conditions, int maxProgress) {
        super(criterion, conditions, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                        @NotNull CruxAdvancementManager manager,
                        @NotNull ObjectiveAdvancement advancement,
                        @NotNull BlockPlaceEvent event){
        LootContext ctx = LootContext.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
