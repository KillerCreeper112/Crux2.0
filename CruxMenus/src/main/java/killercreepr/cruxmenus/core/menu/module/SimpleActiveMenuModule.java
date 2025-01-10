package killercreepr.cruxmenus.core.menu.module;

import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleActiveMenuModule implements ActiveMenuModule {
    protected final @NotNull Key key;
    protected final @NotNull String id;
    public SimpleActiveMenuModule(@NotNull Key key, @NotNull String id) {
        this.key = key;
        this.id = id;
    }
    public SimpleActiveMenuModule(@NotNull Key key) {
        this(key, key.value());
    }
    @Override
    public @NotNull String id() {
        return id;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
