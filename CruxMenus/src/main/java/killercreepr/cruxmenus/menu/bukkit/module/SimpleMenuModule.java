package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleMenuModule implements MenuModule{
    protected final @NotNull Key key;
    public SimpleMenuModule(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
