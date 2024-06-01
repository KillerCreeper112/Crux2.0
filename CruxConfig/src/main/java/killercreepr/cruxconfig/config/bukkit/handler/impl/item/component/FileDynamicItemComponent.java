package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component;

import killercreepr.crux.item.DynamicItemComponent;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import org.jetbrains.annotations.NotNull;

public abstract class FileDynamicItemComponent<T extends DynamicItemComponent> extends SimpleFileHandler<T> {
    protected final @NotNull Class<T> type;
    protected FileDynamicItemComponent(@NotNull Class<T> type) {
        this.type = type;
    }

    public @NotNull Class<T> getType() {
        return type;
    }
}
