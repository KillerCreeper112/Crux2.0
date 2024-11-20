package killercreepr.cruxconfig.config.common.yaml.automatic;

import killercreepr.crux.core.util.CruxReflect;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AutoYamlSerializer<T> implements YamlObjectHandler<T> {
    public static <T> AutoYamlSerializer<T> notNull(@NotNull Class<T> type){
        return new AutoYamlSerializer<>(type, (name, value) -> value != null);
    }

    public static <T> AutoYamlSerializer<T> withDisabledFields(@NotNull Class<T> type, @NotNull String @NotNull... disabledFields){
        Stream<String> stream = Arrays.stream(disabledFields);
        return new AutoYamlSerializer<>(type, null, (field) -> stream.anyMatch(x -> x.equals(field.getName())));
    }

    protected final @NotNull Class<T> type;
    protected final @Nullable BiPredicate<String, Object> isValid;
    protected final @Nullable Predicate<Field> disabledFields;
    public AutoYamlSerializer(@NotNull Class<T> type, @Nullable BiPredicate<String, Object> isValid, @Nullable Predicate<Field> disabledFields) {
        this.type = type;
        this.isValid = isValid;
        this.disabledFields = disabledFields;
    }

    public AutoYamlSerializer(@NotNull Class<T> type, @Nullable BiPredicate<String, Object> isValid) {
        this(type, isValid, null);
    }

    public AutoYamlSerializer(@NotNull Class<T> type) {
        this(type, null, null);
    }

    public @NotNull Class<T> getType() {
        return type;
    }

    /**
     * @return A predicate that determines whether a Field's object
     * is valid when deserializing. If any of the fields are not valid,
     * the deserializeFromYaml method will return null.
     */
    public @Nullable BiPredicate<String, Object> getIsValid() {
        return isValid;
    }

    /**
     * @return A predicate that determines whether a Field should be
     * deserialized and serialized. If the predicate returns
     * true on a Field, that Field will not be saved or loaded.
     */
    public @Nullable Predicate<Field> getDisabledFields() {
        return disabledFields;
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull T object) {
        YamlObject map = new YamlObject();
        YamlRegistry registry = context.getRegistry();
        for(Field field : CruxReflect.getNonStaticDeclaredFields(object.getClass())){
            if(disabledFields != null && disabledFields.test(field)) continue;
            try{
                boolean x = field.canAccess(object);
                field.setAccessible(true);
                Object obj = field.get(object);
                field.setAccessible(x);
                if(obj == null) continue;
                YamlElement serialized = registry.serializeToYaml(obj);
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
            if(disabledFields != null && disabledFields.test(field)) continue;
            Object found  = registry.deserializeFromYaml(field.getType(), yamlMap.get(field.getName()));
            if(isValid != null && !isValid.test(field.getName(), found)) return null;
            fields.put(field.getName(), found);
        }
        return CruxReflect.attemptCreation(type, fields);
    }

}
