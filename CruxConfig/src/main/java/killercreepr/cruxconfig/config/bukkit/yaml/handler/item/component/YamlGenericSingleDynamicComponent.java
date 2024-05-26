package killercreepr.cruxconfig.config.bukkit.yaml.handler.item.component;

import killercreepr.crux.item.components.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface YamlGenericSingleDynamicComponent extends YamlSingleDynamicComponent{
    @Override
    default @Nullable DynamicSingleValueComponent deserialize(@NotNull YamlContext context, @NotNull YamlElement value) {
        return deserialize(context.getRegistry().deserializeObject(value));
    }

    @Nullable DynamicSingleValueComponent deserialize(@NotNull Object object);
}
