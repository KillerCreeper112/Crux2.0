package killercreepr.cruxconfig.config.common;

import org.jetbrains.annotations.NotNull;

public class FileContext<T extends FileRegistry> {
    protected final @NotNull T registry;
    public FileContext(@NotNull T registry) {
        this.registry = registry;
    }

    public @NotNull T getRegistry() {
        return registry;
    }
}
