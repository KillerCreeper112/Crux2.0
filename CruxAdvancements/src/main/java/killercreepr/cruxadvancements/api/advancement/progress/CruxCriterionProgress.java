package killercreepr.cruxadvancements.api.advancement.progress;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface CruxCriterionProgress  {
    boolean isDone();
    void grant();
    void revoke();
    @Nullable
    Instant getObtained();
    @ApiStatus.Internal
    void setObtainedAt(@Nullable Instant obtained);
}
