package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class BaseSimplePersistentParser<T> implements SimplePersistentParser<T>{
    protected final @NotNull Key key;
    public BaseSimplePersistentParser(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
