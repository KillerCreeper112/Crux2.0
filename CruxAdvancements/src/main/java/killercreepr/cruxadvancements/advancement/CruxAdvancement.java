package killercreepr.cruxadvancements.advancement;

import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface CruxAdvancement extends Keyed {
    @Nullable
    CruxAdvanceReward reward();
    default @Nullable CruxAdvancementProgress getProgress(@NotNull Player p){
        return getProgress(p.getUniqueId());
    }
    @NotNull
    CruxAdvancementProgress getProgress(@NotNull UUID uuid);
    @NotNull
    CruxCriteria getCriteria();

    default boolean isGranted(@NotNull Player p){
        return isGranted(p.getUniqueId());
    }
    boolean isGranted(@NotNull UUID uuid);
}
