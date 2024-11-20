package killercreepr.cruxadvancements.core.config.handler;

import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.core.advancement.objective.progress.SimpleObjectiveProgression;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSimpleObjectiveProgression implements FileObjectHandler<SimpleObjectiveProgression> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull SimpleObjectiveProgression object) {
        FileRegistry registry = ctx.getRegistry();
        FileObject o = new FileObject();
        object.getProgressMap().forEach((key, value) ->{
            o.add(key, registry.serializeToFile(value));
        });
        return o;
    }

    @Override
    public @Nullable SimpleObjectiveProgression deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        return null;
    }

    public static @Nullable SimpleObjectiveProgression deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                              @NotNull ObjectiveAdvancement a) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        SimpleObjectiveProgression objective = new SimpleObjectiveProgression(a);
        o.forEach((key, value) ->{
            ObjectiveProgress progress = registry.deserializeFromFile(ObjectiveProgress.class, value);
            if(progress==null) return;
            objective.getProgressMap().put(key, progress);
        });
        return objective;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_objective_progression";
    }
}
