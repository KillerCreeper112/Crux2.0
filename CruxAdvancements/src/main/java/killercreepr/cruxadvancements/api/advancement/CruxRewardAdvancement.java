package killercreepr.cruxadvancements.api.advancement;

import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import org.jetbrains.annotations.Nullable;

public interface CruxRewardAdvancement extends CruxAdvancement{
    @Nullable CruxAdvanceReward getReward();
}
