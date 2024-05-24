package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlGenericEnum<T extends Enum<T>> implements YamlObjectHandler<T> {
    protected final Class<T> clazz;
    public YamlGenericEnum(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        return new YamlPrimitive(object.toString().toLowerCase());
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlPrimitive s) || !s.isString()) return null;
        try{
            return Enum.valueOf(clazz, s.getAsString().toUpperCase());
        }catch (IllegalArgumentException ignored){
            return null;
        }
    }
}
