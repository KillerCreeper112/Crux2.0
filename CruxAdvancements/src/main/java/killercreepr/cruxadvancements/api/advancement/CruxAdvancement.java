package killercreepr.cruxadvancements.api.advancement;

import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.api.advancement.icon.CruxAdvancementIcon;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface CruxAdvancement extends Keyed {
    @Nullable
    Key parent();
    @Nullable
    CruxAdvanceReward reward();
    default @NotNull CruxAdvancementProgress getProgress(@NotNull Player p){
        return getProgress(p.getUniqueId());
    }
    @NotNull
    CruxAdvancementProgress getProgress(@NotNull UUID uuid);
    @Nullable CruxAdvancementProgress getProgressIfPresent(@NotNull UUID uuid);
    @NotNull
    Map<String, CruxAdvancementProgress> getProgressMap();
    void setProgress(@NotNull UUID uuid, @Nullable CruxAdvancementProgress progress);
    @NotNull
    CruxCriteria getCriteria();

    default boolean isGranted(@NotNull Player p){
        return isGranted(p.getUniqueId());
    }
    boolean isGranted(@NotNull UUID uuid);

    @NotNull
    CruxAdvancementIcon getIcon();

    default void onSaving(CruxAdvancementManager<?> manager){

    }
    default void onLoading(CruxAdvancementManager<?> manager){
    }
}
