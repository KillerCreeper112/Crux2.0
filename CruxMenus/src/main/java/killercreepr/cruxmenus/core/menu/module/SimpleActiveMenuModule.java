package killercreepr.cruxmenus.core.menu.module;

import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import org.jetbrains.annotations.NotNull;

public class SimpleActiveMenuModule implements ActiveMenuModule {
    protected final @NotNull String id;
    protected final @NotNull MenuModule module;
    public SimpleActiveMenuModule(@NotNull String id, @NotNull MenuModule module) {
        this.id = id;
        this.module = module;
    }

    @Override
    public @NotNull String id() {
        return id;
    }

    @Override
    public @NotNull MenuModule getModule() {
        return module;
    }
}
