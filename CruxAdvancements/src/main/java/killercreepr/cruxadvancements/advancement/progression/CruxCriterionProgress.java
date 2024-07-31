package killercreepr.cruxadvancements.advancement.progression;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface CruxCriterionProgress  {
    boolean isDone();
    void grant();
    void revoke();
    @Nullable
    Instant getObtained();
}
