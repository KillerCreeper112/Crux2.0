package killercreepr.cruxadvancements.core.config.handler;

import killercreepr.cruxadvancements.core.advancement.progress.SimpleCriterionProgress;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class FileSimpleCriterionProgress implements FileObjectHandler<SimpleCriterionProgress> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull SimpleCriterionProgress object) {
        FileRegistry registry = ctx.getRegistry();
        FileObject o = new FileObject();
        Instant obtained = object.getObtained();
        if(obtained != null){
            o.add("obtained", registry.serializeToFile(obtained));
        }
        return o;
    }

    @Override
    public @Nullable SimpleCriterionProgress deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Instant obtained = registry.deserializeFromFile(Instant.class, o.get("obtained"));
        if(obtained==null) return null;
        return new SimpleCriterionProgress(obtained);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_criterion_progress";
    }
}
