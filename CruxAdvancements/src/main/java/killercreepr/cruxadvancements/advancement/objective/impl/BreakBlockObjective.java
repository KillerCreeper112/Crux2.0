package killercreepr.cruxadvancements.advancement.objective.impl;

import killercreepr.crux.loot.impl.SimpleLootContext;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BreakBlockObjective extends NumberObjective {
    protected final @Nullable Material blockType;
    public BreakBlockObjective(@NotNull String criterion, @Nullable LootCondition conditions, int maxProgress, @Nullable Material blockType) {
        super(criterion, conditions, maxProgress);
        this.blockType = blockType;
    }

    public @Nullable Material getBlockType() {
        return blockType;
    }

    public boolean canTrigger(@NotNull Material m){
        if(blockType == null) return true;
        return m == blockType;
    }

    public boolean trigger(@NotNull UUID who,
                        @NotNull CruxAdvancementManager manager,
                        @NotNull ObjectiveAdvancement advancement,
                        @NotNull BlockBreakEvent event){
        LootContext ctx = SimpleLootContext.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
