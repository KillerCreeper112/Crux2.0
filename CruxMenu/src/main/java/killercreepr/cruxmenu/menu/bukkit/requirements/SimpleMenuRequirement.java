package killercreepr.cruxmenu.menu.bukkit.requirements;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleMenuRequirement implements MenuRequirement {
    protected final NamespacedKey key;
    public SimpleMenuRequirement(@NotNull NamespacedKey key) {
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
