package killercreepr.cruxmenus.menu.bukkit.requirements;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleMenuRequirement implements MenuRequirement {
    protected final Key key;
    public SimpleMenuRequirement(@NotNull Key key) {
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
