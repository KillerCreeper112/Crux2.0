package killercreepr.cruxconfig.config.common.handler;

import killercreepr.crux.util.CruxReflect;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AutoFileHandler<T> extends SimpleFileHandler<T> {
    public static <T> AutoFileHandler<T> notNull(@NotNull Class<T> type){
        return new AutoFileHandler<>(type, (name, value) -> value != null);
    }

    public static <T> AutoFileHandler<T> withDisabledFields(@NotNull Class<T> type, @NotNull String @NotNull... disabledFields){
        Stream<String> stream = Arrays.stream(disabledFields);
        return new AutoFileHandler<>(type, null, (field) -> stream.anyMatch(x -> x.equals(field.getName())));
    }

    protected final @NotNull Class<T> type;
    protected final @Nullable BiPredicate<String, Object> isValid;
    protected final @Nullable Predicate<Field> disabledFields;
    public AutoFileHandler(@NotNull Class<T> type, @Nullable BiPredicate<String, Object> isValid, @Nullable Predicate<Field> disabledFields) {
        this.type = type;
        this.isValid = isValid;
        this.disabledFields = disabledFields;
    }

    public AutoFileHandler(@NotNull Class<T> type, @Nullable BiPredicate<String, Object> isValid) {
        this(type, isValid, null);
    }

    public AutoFileHandler(@NotNull Class<T> type) {
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
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        FileObject map = new FileObject();
        FileRegistry registry = context.getRegistry();
        for(Field field : CruxReflect.getNonStaticDeclaredFields(object.getClass())){
            if(disabledFields != null && disabledFields.test(field)) continue;
            try{
                boolean x = field.canAccess(object);
                field.setAccessible(true);
                Object obj = field.get(object);
                field.setAccessible(x);
                if(obj == null) continue;
                FileElement serialized = registry.serializeToFileElement(obj);
                map.add(field.getName(), serialized);
            }catch (IllegalAccessException ignored){}
        }
        return map;
    }

    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        Map<String, Object> fields = new LinkedHashMap<>();
        Map<String, FileElement> yamlMap = o.asMap();
        for(Field field : CruxReflect.getNonStaticDeclaredFields(type)){
            if(disabledFields != null && disabledFields.test(field)) continue;
            Object found  = registry.deserialize(field.getType(), yamlMap.get(field.getName()));
            if(isValid != null && !isValid.test(field.getName(), found)) return null;
            fields.put(field.getName(), found);
        }
        return CruxReflect.attemptCreation(type, fields);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return type.getSimpleName().toLowerCase();
    }
}
