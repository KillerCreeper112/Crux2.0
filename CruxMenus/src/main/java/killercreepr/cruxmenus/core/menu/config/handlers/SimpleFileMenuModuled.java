package killercreepr.cruxmenus.core.menu.config.handlers;

import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuModuled;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleFileMenuModuled<T> implements FileMenuModuled<T> {
    protected final @NotNull FileMenuHolder<?> menuModule;
    public SimpleFileMenuModuled(@NotNull FileMenuHolder<?> menuModule) {
        this.menuModule = menuModule;
    }

    @Override
    public @NotNull FileMenuHolder<?> menuModule() {
        return menuModule;
    }
}
