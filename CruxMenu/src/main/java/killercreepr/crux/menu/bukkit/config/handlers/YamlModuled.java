package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YamlModuled<T> implements YamlObjectHandler<T> {
    protected final @NotNull YamlMenuModule menuModule;
    public YamlModuled(@NotNull YamlMenuModule menuModule) {
        this.menuModule = menuModule;
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not have a serialization method implemented!");
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        return deserializeFromYaml(context, e, null);
    }

    public abstract @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable YamlObject menuContext);
}
