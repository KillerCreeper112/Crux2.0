package killercreepr.cruxmenus.menu.bukkit.config.handlers;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileModuled<T> extends SimpleFileHandler<T> {
    protected final @NotNull FileMenuModule menuModule;
    public FileModuled(@NotNull FileMenuModule menuModule) {
        this.menuModule = menuModule;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not have a serialization method implemented!");
    }

    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return deserializeFromFile(context, e, null);
    }

    public abstract @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext);

    /*@Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not have a serialization method implemented!");
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        return deserializeFromYaml(context, e, null);
    }*/

    //public abstract @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable YamlObject menuContext);
}
