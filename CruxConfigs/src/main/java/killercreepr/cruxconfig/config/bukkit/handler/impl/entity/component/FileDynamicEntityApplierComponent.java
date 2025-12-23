package killercreepr.cruxconfig.config.bukkit.handler.impl.entity.component;

import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierComponent;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import org.jetbrains.annotations.NotNull;

public abstract class FileDynamicEntityApplierComponent<T extends DynamicEntityApplierComponent> extends SimpleFileHandler<T> {
    protected final @NotNull Class<T> type;
    protected FileDynamicEntityApplierComponent(@NotNull Class<T> type) {
        this.type = type;
    }

    public @NotNull Class<T> getType() {
        return type;
    }
}
