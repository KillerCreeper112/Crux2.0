package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class GenericEventObjective<T extends Event> extends NumberObjective {
    public GenericEventObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    protected abstract LootContext buildContext(T event);

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull T event){
        return trigger(who, manager, advancement, event, 1);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull T event, int amount){
        LootContext ctx = buildContext(event);
        return trigger(
            who, manager, advancement, ctx, amount
        );
    }
}
