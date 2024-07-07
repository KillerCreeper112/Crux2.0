package killercreepr.cruxadvancements.advancement;

import org.jetbrains.annotations.NotNull;

public interface CruxAdvance {
    @NotNull CruxAdvancement getParent();
    float getProgress();
}
