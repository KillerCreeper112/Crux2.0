package killercreepr.cruxadvancements.advancement.reward;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface CruxAdvanceReward {
    void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull UUID who);
}
