package killercreepr.cruxadvancements.config.handler;

import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomFileObjectiveProgress<T extends ObjectiveProgress> {
    @NotNull String getType();

    default @NotNull FileElement attemptSerializeToFile(@NotNull FileContext<?> ctx, @NotNull ObjectiveProgress object){
        return serializeToFile(ctx, (T) object);
    }

    @NotNull
    FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull T object);
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e);
}
