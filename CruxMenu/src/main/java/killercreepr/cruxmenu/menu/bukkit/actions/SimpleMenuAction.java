package killercreepr.cruxmenu.menu.bukkit.actions;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleMenuAction implements MenuAction {
    protected final Key key;
    public SimpleMenuAction(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public boolean has(@NotNull String x) {
        return x.equalsIgnoreCase(key.value());
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
