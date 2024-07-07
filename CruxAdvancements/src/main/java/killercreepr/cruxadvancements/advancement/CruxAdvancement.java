package killercreepr.cruxadvancements.advancement;

import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;

public interface CruxAdvancement extends Keyed {
    @Nullable
    CruxAdvanceReward reward();
}
