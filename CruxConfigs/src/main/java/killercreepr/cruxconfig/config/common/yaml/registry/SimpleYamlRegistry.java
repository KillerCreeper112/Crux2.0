package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.cruxconfig.config.common.base.BaseFileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class SimpleYamlRegistry extends BaseFileRegistry implements YamlRegistry {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull Object object) {
        return serializeToFile(object).toYaml();
    }

    @Override
    public <T> @Nullable T deserializeFromYaml(@NotNull Type type, @Nullable YamlElement o) {
        if(o==null) return null;
        return deserializeFromFile(type, FileElement.fromYaml(o));
    }

    @Override
    public <T> @Nullable T deserializeFromYaml(@NotNull Class<T> clazz, @Nullable YamlElement o) {
        if(o==null) return null;
        return deserializeFromFile(clazz, FileElement.fromYaml(o));
    }

    @Override
    public <T> @Nullable T deserializeFromYaml(@NotNull Class<T> clazz, @Nullable YamlElement o, @NotNull YamlContext context) {
        if(o==null) return null;
        return deserializeFromFile(clazz, FileElement.fromYaml(o), context);
    }

    @Override
    public @Nullable Object deserializeObjectFromYaml(@NotNull YamlElement o) {
        return deserializeObjectFromFile(FileElement.fromYaml(o));
    }
}
