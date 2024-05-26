package killercreepr.cruxconfig.config.bukkit.yaml.handler.generic;

import killercreepr.crux.item.DynamicItemComponent;
import killercreepr.crux.item.components.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.bukkit.yaml.handler.YamlDynamicItemComponent;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlSingleDynamicComponent<T extends DynamicSingleValueComponent> implements YamlDynamicItemComponent<T> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        return new YamlGeneric(object.getValue());
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlGeneric s)) return null;
        return new DynamicSingleValueComponent(s.getAsString());
    }
}
