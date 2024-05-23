package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YamlGenericKeyed<T extends Keyed> implements YamlObjectHandler<T> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        return new YamlPrimitive(object.getKey().asString());
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlPrimitive s) || !s.isString()) return null;
        NamespacedKey key = parseKey(s);
        if(key==null) return null;
        return deserializeFromKey(key);
    }

    public abstract @Nullable T deserializeFromKey(@NotNull NamespacedKey key);

    public @Nullable NamespacedKey parseKey(@NotNull YamlPrimitive e){
        return NamespacedKey.fromString(e.getAsString());
    }
}
