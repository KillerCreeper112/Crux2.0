package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class KillEntityObjective extends NumberObjective {
    protected final @Nullable EntityType entityType;
    public KillEntityObjective(@NotNull String criterion, int maxProgress, @Nullable EntityType entityType) {
        super(criterion, maxProgress);
        this.entityType = entityType;
    }

    public @Nullable EntityType getEntityType() {
        return entityType;
    }

    public boolean canTrigger(@NotNull EntityType m){
        if(entityType == null) return true;
        return m == entityType;
    }

    public void trigger(@NotNull UUID who,
                              @NotNull CruxAdvancementManager manager,
                              @NotNull ObjectiveAdvancement advancement,
                              @NotNull EntityType m){
        if(!canTrigger(m)) return;
        addToProgress(who, manager, advancement,1);
    }
}
