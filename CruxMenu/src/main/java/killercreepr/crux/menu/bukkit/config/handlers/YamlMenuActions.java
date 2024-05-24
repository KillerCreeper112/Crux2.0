package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class YamlMenuActions implements YamlObjectHandler<Map<ClickType, Collection<String>>> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull Map<ClickType, Collection<String>> object) {
        return null;
    }

    @Override
    public @Nullable Map<ClickType, Collection<String>> deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        Map<ClickType, Collection<String>> map = new HashMap<>();
        return map.isEmpty()?null:map;
    }
}
