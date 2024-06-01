package killercreepr.cruxconfig.config.bukkit.handler;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.json.JsonContext;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleFileHandler<T> implements FileHandler<T>{

    @Override
    public @NotNull FileElement serializeToFileFromJson(@NotNull JsonContext context, @NotNull T object){
        return serializeToFile(context, object);
    }
    @Override
    public @NotNull FileElement serializeToFileFromYaml(@NotNull YamlContext context, @NotNull T object){
        return serializeToFile(context, object);
    }

    public abstract @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object);
    public abstract @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e);

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(e==null) return null;
        return deserializeFromFile(context, FileElement.fromYaml(e));
    }

    @Override
    public @Nullable T deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement e) {
        if(e==null) return null;
        return deserializeFromFile(context, FileElement.fromJson(e));
    }
}
