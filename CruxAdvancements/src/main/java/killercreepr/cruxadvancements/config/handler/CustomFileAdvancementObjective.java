package killercreepr.cruxadvancements.config.handler;

import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomFileAdvancementObjective<T extends AdvancementObjective> {
    @NotNull String getType();
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data);
}
