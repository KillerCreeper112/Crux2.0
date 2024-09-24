package killercreepr.cruxmenus.api.menu.config.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileMenuModuled<T> extends FileObjectHandler<T> {
    @NotNull FileMenuHolder<?> menuModule();

    @Override
    default @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not have a serialization method implemented!");
    }

    @Override
    default @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return deserializeFromFile(context, e, null);
    }

    @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext);
}
