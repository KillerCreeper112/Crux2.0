package killercreepr.cruxadvancements.advancement.reward;

import killercreepr.cruxadvancements.event.CruxAdvancementRewardEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CruxAdvanceReward {
    @NotNull
    CruxAdvancementRewardEvent reward(@NotNull Player p);
}
