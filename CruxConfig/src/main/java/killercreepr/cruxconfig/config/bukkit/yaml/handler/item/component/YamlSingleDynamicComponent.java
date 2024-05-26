package killercreepr.cruxconfig.config.bukkit.yaml.handler.item.component;

import killercreepr.crux.item.components.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YamlSingleDynamicComponent<T extends DynamicSingleValueComponent>  extends YamlDynamicItemComponent<T> {
    protected YamlSingleDynamicComponent(@NotNull Class<T> type) {
        super(type);
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull DynamicSingleValueComponent object) {
        return context.getRegistry().serializeObject(object.getValue());
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(e==null) return null;
        return deserialize(context, e);
    }

    public abstract @Nullable T deserialize(@NotNull YamlContext context, @NotNull YamlElement value);
}
