package killercreepr.cruxconfig.config.common.yaml.automatic;

import killercreepr.crux.util.CruxReflect;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.container.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class AutoYamlSerializer<T> implements YamlObjectHandler<T> {
    protected final Class<T> type;
    public AutoYamlSerializer(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        YamlObject map = new YamlObject();
        YamlRegistry registry = context.getRegistry();
        for(Field field : CruxReflect.getNonStaticDeclaredFields(object.getClass())){
            try{
                boolean x = field.canAccess(object);
                field.setAccessible(true);
                Object obj = field.get(object);
                field.setAccessible(x);
                if(obj == null) continue;
                YamlElement serialized = registry.serializeObject(obj);
                map.add(field.getName(), serialized);
            }catch (IllegalAccessException ignored){}
        }
        return map;
    }

    @Override
    public @Nullable T deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        YamlRegistry registry = context.getRegistry();
        Map<String, Object> fields = new LinkedHashMap<>();
        Map<String, YamlElement> yamlMap = o.asMap();
        for(Field field : CruxReflect.getNonStaticDeclaredFields(type)){
            Object found  = registry.deserialize(field.getType(), yamlMap.get(field.getName()));
            if(found == null && field.getAnnotation(NotNull.class) != null) return null;
            fields.put(field.getName(), found);
        }
        return CruxReflect.attemptCreation(type, fields);
    }
}
