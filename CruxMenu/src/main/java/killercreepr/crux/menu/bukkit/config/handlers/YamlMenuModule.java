package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.menu.bukkit.holder.MenuHolder;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlMenuModule implements YamlObjectHandler<MenuHolder> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull MenuHolder object) {
        return null;
    }

    @Override
    public @Nullable MenuHolder deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        return null;
    }
}
