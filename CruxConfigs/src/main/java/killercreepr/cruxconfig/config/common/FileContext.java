package killercreepr.cruxconfig.config.common;

import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class FileContext<T extends FileRegistry> implements IFileContext {
    protected final @NotNull T registry;
    public FileContext(@NotNull T registry) {
        this.registry = registry;
    }

    public @NotNull T getRegistry() {
        return registry;
    }

    @Override
    public @Nullable Object deserializeObjectRaw(@NotNull Type type, @NotNull FileElement from) {
        return null;
    }
}
