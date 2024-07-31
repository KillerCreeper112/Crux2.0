package killercreepr.cruxadvancements.advancement.progression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public interface CruxAdvancementProgress extends CruxCriterionProgress {
    @NotNull GrantCriteriaResult grantCriteria(@NotNull String... criteria);
    @NotNull GenericResult revokeCriteria(@NotNull String... criteria);

    @NotNull GrantCriteriaResult setCriteriaProgress(int amount);
    int getCriteriaProgress();
    @Nullable CruxCriterionProgress getCriterionProgress(@NotNull String name);
    @NotNull
    Collection<String> getAwardedCriteria();
    @NotNull Collection<String> getRemainingCriteria();
    int getCriteriaMaxProgress();

    int count(@NotNull Predicate<String> predicate);
    @NotNull Collection<String> assemble(@NotNull Predicate<String> predicate);
}
