package killercreepr.cruxconfig.config.bukkit.yaml.handler.item.component;

import killercreepr.crux.item.components.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface YamlSingleDynamicComponent extends YamlDynamicItemComponent<DynamicSingleValueComponent> {

    @Override
    default @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull DynamicSingleValueComponent object) {
        return context.getRegistry().serializeObject(object.getValue());
    }

    @Override
    default @Nullable DynamicSingleValueComponent deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(e==null) return null;
        return deserialize(context, e);
    }

    @Nullable DynamicSingleValueComponent deserialize(@NotNull YamlContext context, @NotNull YamlElement value);
}
