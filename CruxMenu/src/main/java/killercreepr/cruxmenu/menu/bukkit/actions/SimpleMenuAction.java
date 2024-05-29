package killercreepr.cruxmenu.menu.bukkit.actions;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleMenuAction implements MenuAction {
    protected final NamespacedKey key;
    public SimpleMenuAction(@NotNull NamespacedKey key) {
        this.key = key;
    }

    @Override
    public boolean has(@NotNull String x) {
        return x.equalsIgnoreCase(key.getKey());
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }
}
