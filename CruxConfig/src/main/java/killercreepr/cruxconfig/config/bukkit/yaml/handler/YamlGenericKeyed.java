package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YamlGenericKeyed<T extends Keyed> implements YamlObjectHandler<T> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        return new YamlPrimitive(object.key().asString());
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlPrimitive s) || !s.isString()) return null;
        Key key = parseKey(s);
        if(key==null) return null;
        return deserializeFromKey(key);
    }

    public abstract @Nullable T deserializeFromKey(@NotNull Key key);

    public @Nullable Key parseKey(@NotNull YamlPrimitive e){
        return Key.key(e.getAsString());
    }
}
