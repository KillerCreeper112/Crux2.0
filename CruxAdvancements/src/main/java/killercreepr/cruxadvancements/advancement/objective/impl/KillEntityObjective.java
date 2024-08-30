package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class KillEntityObjective extends NumberObjective {
    public KillEntityObjective(@NotNull String criterion, @Nullable LootCondition conditions, int maxProgress) {
        super(criterion, conditions, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull EntityDamageByEntityEvent event){
        LootContext ctx = LootContext.builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getDamager(), "damager")
                    .putAll(event.getEntity(), "victim", "entity")
                    .build()
            )
            .looter(event.getDamager())
            .looted(event.getEntity())
            .location(event.getEntity().getLocation())
            .build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
