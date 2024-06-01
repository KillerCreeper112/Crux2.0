package killercreepr.cruxconfig.config.bukkit.handler;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.json.JsonContext;
import killercreepr.cruxconfig.config.common.json.container.JsonContainerHandler;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;

public interface FileHandler<T> extends YamlObjectHandler<T>, JsonContainerHandler<T> {

    @NotNull FileElement serializeToFileFromJson(@NotNull JsonContext context, @NotNull T object);
    @NotNull FileElement serializeToFileFromYaml(@NotNull YamlContext context, @NotNull T object);

    @Override
    default @NotNull JsonElement serializeToJson(@NotNull JsonContext context, @NotNull T object) {
        return serializeToFileFromJson(context, object).toJson();
    }

    @Override
    default @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        return serializeToFileFromYaml(context, object).toYaml();
    }
}
