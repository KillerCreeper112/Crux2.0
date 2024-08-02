package killercreepr.cruxadvancements.advancement.reward;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CruxAdvanceReward {
    void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull Player p);
}
