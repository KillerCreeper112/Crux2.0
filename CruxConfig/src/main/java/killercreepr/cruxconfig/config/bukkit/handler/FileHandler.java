package killercreepr.cruxconfig.config.bukkit.handler;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.json.JsonContext;
import killercreepr.cruxconfig.config.common.json.container.JsonContainerHandler;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;

public abstract class FileHandler<T> implements YamlObjectHandler<T>, JsonContainerHandler<T> {

    public abstract @NotNull FileElement serializeToFileFromJson(@NotNull JsonContext context, @NotNull T object);
    public abstract @NotNull FileElement serializeToFileFromYaml(@NotNull YamlContext context, @NotNull T object);

    @Override
    public @NotNull JsonElement serializeToJson(@NotNull JsonContext context, @NotNull T object) {
        return serializeToFileFromJson(context, object).toJson();
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        return serializeToFileFromYaml(context, object).toYaml();
    }
}
