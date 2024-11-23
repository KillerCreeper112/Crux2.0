package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class KillEntityObjective extends NumberObjective {
    public KillEntityObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull EntityDeathEvent event){
        LootContext ctx = EventLootContexts.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull EntityDamageByEntityEvent event){
        LootContext ctx = EventLootContexts.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
