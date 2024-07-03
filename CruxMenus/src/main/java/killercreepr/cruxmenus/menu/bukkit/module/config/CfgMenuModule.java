package killercreepr.cruxmenus.menu.bukkit.module.config;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.module.ActiveMenuModule;
import killercreepr.cruxmenus.menu.bukkit.module.SimpleMenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CfgMenuModule extends SimpleMenuModule {
    protected final @NotNull String id;
    public CfgMenuModule(@NotNull Key key, @NotNull String id) {
        super(key);
        this.id = id;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return build(menu, id);
    }

    public abstract @Nullable ActiveMenuModule build(@NotNull Menu menu, @NotNull String id);
}
