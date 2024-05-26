package killercreepr.cruxconfig.config.bukkit.yaml.handler.item.component;

import killercreepr.crux.item.components.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YamlGenericSingleDynamicComponent<T extends DynamicSingleValueComponent> extends YamlSingleDynamicComponent<T>{
    protected YamlGenericSingleDynamicComponent(@NotNull Class<T> type) {
        super(type);
    }

    @Override
    public @Nullable T deserialize(@NotNull YamlContext context, @NotNull YamlElement value) {
        return deserialize(context.getRegistry().deserializeObject(value));
    }

    public abstract @Nullable T deserialize(@NotNull Object object);
}
