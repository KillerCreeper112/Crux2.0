package killercreepr.cruxadvancements.config.handler;

import killercreepr.cruxadvancements.advancement.objective.condition.ObjectiveCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomFileObjectiveCondition<T extends ObjectiveCondition> {
    @NotNull String getType();
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target);
}
