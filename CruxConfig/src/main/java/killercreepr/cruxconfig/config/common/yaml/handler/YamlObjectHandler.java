package killercreepr.cruxconfig.config.common.yaml.handler;

import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface YamlObjectHandler<T> {
    default @Nullable YamlElement attemptSerializeToYaml(@NotNull YamlContext context, @NotNull Object object){
        try{
            return serializeToYaml(context, (T) object);
        }catch (ClassCastException ignored){ return null; }
    }
    @NotNull
    YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object);
    @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e);
}
