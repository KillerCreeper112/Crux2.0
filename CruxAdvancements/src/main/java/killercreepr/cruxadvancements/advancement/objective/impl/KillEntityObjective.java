package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.SimpleLootContext;
import killercreepr.crux.loot.api.LootContext;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.advancement.objective.condition.ObjectiveConditions;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class KillEntityObjective extends NumberObjective {
    public KillEntityObjective(@NotNull String criterion, @Nullable ObjectiveConditions conditions, int maxProgress) {
        super(criterion, conditions, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull EntityDamageByEntityEvent event){
        LootContext ctx = SimpleLootContext.builder()
            .setInfo(
                DataExchange.builder()
                    .putAll(event.getDamager(), "damager")
                    .putAll(event.getEntity(), "victim", "entity")
                    .build()
            )
            .setLooter(event.getDamager())
            .setLooted(event.getEntity())
            .setLocation(event.getEntity().getLocation())
            .build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
