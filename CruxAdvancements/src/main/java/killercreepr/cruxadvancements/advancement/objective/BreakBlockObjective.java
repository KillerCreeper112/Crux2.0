package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BreakBlockObjective extends NumberObjective{
    protected final @Nullable Material blockType;
    public BreakBlockObjective(@NotNull String criterion, int maxProgress, @Nullable Material blockType) {
        super(criterion, maxProgress);
        this.blockType = blockType;
    }

    public @Nullable Material getBlockType() {
        return blockType;
    }

    public boolean canTrigger(@NotNull Material m){
        if(blockType == null) return true;
        return m == blockType;
    }

    public void trigger(@NotNull UUID who,
                              @NotNull CruxAdvancementManager<ObjectiveAdvancement> manager,
                              @NotNull ObjectiveAdvancement advancement,
                              String criterion,
                              @NotNull ObjectiveProgress progress,
                              @NotNull Material m){
        if(!canTrigger(m)) return;
        addToProgress(who, manager, advancement, criterion, progress, 1);
    }
}
