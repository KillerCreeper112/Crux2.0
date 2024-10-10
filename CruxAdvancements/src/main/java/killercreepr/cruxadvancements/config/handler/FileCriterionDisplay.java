package killercreepr.cruxadvancements.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxadvancements.advancement.icon.CriterionDisplay;
import killercreepr.cruxadvancements.advancement.icon.SimpleCriterionDisplay;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class FileCriterionDisplay implements FileObjectHandler<CriterionDisplay> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CriterionDisplay object) {
        FileRegistry registry = ctx.getRegistry();
        if(object instanceof SimpleCriterionDisplay d){
            return registry.serializeToFile(d.getMap());
        }
        throw new NullPointerException("unsupported");
    }

    @Override
    public @Nullable CriterionDisplay deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        Map<String, String> map = registry.deserializeFromFile(
            new TypeToken<LinkedHashMap<String, String>>(){}.getType(), e
        );
        if(map == null || map.isEmpty()) return null;
        return new SimpleCriterionDisplay(map);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "criterion_display";
    }
}
