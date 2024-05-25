package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlComponent implements YamlObjectHandler<Component> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull Component object) {
        return new YamlPrimitive(Crux.FORMAT.serialize(object));
    }

    @Override
    public @Nullable Component deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlPrimitive s)) return null;
        return Crux.FORMAT.deserialize(s.getAsString());
    }
}
