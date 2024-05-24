package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlMaterial implements YamlObjectHandler<Material> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull Material object) {
        return new YamlPrimitive(object.toString().toLowerCase());
    }

    @Override
    public @Nullable Material deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlPrimitive s) || !s.isString()) return null;
        return Material.matchMaterial(s.getAsString());
    }
}
