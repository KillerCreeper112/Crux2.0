package killercreepr.cruxconfig.config.bukkit.yaml.handler.item.component;

import killercreepr.crux.item.DynamicItemComponent;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;

public abstract class YamlDynamicItemComponent<T extends DynamicItemComponent> implements YamlObjectHandler<T> {
    protected final @NotNull Class<T> type;
    protected YamlDynamicItemComponent(@NotNull Class<T> type) {
        this.type = type;
    }

    public @NotNull Class<T> getType() {
        return type;
    }
}
