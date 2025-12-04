package killercreepr.cruxadvancements.core.advancement.progress;

import killercreepr.cruxadvancements.api.advancement.progress.CruxCriterionProgress;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class SimpleCriterionProgress implements CruxCriterionProgress {
    protected @Nullable Instant obtained;
    public SimpleCriterionProgress() {}

    public SimpleCriterionProgress(@Nullable Instant obtainedTime) {
        this.obtained = obtainedTime;
    }

    @Override
    public boolean isDone() {
        return this.obtained != null;
    }
    @Override
    public void grant() {
        this.obtained = Instant.now();
    }
    @Override
    public void revoke() {
        this.obtained = null;
    }
    @Override
    @Nullable
    public Instant getObtained() {
        return this.obtained;
    }

    @Override
    public void setObtainedAt(@Nullable Instant obtained) {
        this.obtained = obtained;
    }

    @Override
    public String toString() {
        return "SimpleAdvancementProgress{obtained=" + (this.obtained == null ? "false" : this.obtained) + "}";
    }
}
