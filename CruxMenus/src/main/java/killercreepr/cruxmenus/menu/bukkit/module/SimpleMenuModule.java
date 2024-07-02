package killercreepr.cruxmenus.menu.bukkit.module;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleMenuModule implements MenuModule{
    protected final @NotNull Key key;
    protected final @NotNull String id;
    public SimpleMenuModule(@NotNull Key key, @NotNull String id) {
        this.key = key;
        this.id = id;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull String id() {
        return id;
    }
}
