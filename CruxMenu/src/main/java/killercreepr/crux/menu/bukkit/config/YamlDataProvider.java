package killercreepr.crux.menu.bukkit.config;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface YamlDataProvider {
    static @NotNull YamlDataProvider generic(@NotNull Class<?> parseClass){
        return new YamlDataProvider() {
            @Override
            public @Nullable Object deserialize(@NotNull YamlContext context, @NotNull YamlElement base, @Nullable YamlObject menuContext, @NotNull YamlElement e) {
                return context.getRegistry().deserializeObject(parseClass, e, context);
            }
        };
    }

    @Nullable Object deserialize(@NotNull YamlContext context, @NotNull YamlElement base, @Nullable YamlObject menuContext,
                                 @NotNull YamlElement e);
}
