package killercreepr.cruxmenus.menu.bukkit.module;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

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
