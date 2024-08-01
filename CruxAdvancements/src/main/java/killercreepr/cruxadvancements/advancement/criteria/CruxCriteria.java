package killercreepr.cruxadvancements.advancement.criteria;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface CruxCriteria {
    boolean test(@NotNull Predicate<String> predicate);
}
