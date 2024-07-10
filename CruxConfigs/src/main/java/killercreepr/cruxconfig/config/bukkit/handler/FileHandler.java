package killercreepr.cruxconfig.config.bukkit.handler;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.json.JsonContext;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializerID;
import killercreepr.cruxconfig.config.common.json.container.JsonContainerHandler;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileHandler<T> extends YamlObjectHandler<T>, JsonContainerHandler<T>, JsonSerializerID {

    default @NotNull FileElement serializeToFileFromJson(@NotNull JsonContext context, @NotNull T object){
        return serializeToFile(context, object);
    }
    default @NotNull FileElement serializeToFileFromYaml(@NotNull YamlContext context, @NotNull T object){
        return serializeToFile(context, object);
    }
    @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object);
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e);

    @Override
    default @NotNull JsonElement serializeToJson(@NotNull JsonContext context, @NotNull T object) {
        return serializeToFileFromJson(context, object).toJson();
    }

    @Override
    default @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        return serializeToFileFromYaml(context, object).toYaml();
    }

    @Override
    default @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(e==null) return null;
        return deserializeFromFile(context, FileElement.fromYaml(e));
    }

    @Override
    default @Nullable T deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement e) {
        if(e==null) return null;
        return deserializeFromFile(context, FileElement.fromJson(e));
    }
}
