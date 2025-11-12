package killercreepr.cruxconfig.config.common.handler;

import killercreepr.crux.core.util.CruxObjects;
import killercreepr.crux.core.util.CruxReflect;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AutoFileHandler<T> extends SimpleFileHandler<T> {
    public static <T> AutoFileHandler<T> notNull(@NotNull Class<T> type){
        return new AutoFileHandler<>(
            type, AutoFileOptions.builder()
            .isValid((name, value) -> value != null)
            .build()
        );
    }

    public static <T> AutoFileHandler<T> withDisabledFields(@NotNull Class<T> type, @NotNull String @NotNull... disabledFields){
        Stream<String> stream = Arrays.stream(disabledFields);
        return new AutoFileHandler<>(
            type, AutoFileOptions.builder()
            .disabledFields((field) -> stream.anyMatch(x -> x.equals(field.getName())))
            .build()
        );
    }

    protected final @NotNull Class<T> type;
    protected final @Nullable AutoFileOptions options;

    public AutoFileHandler(@NotNull Class<T> type, @Nullable AutoFileOptions options) {
        this.type = type;
        this.options = options;
    }

    public AutoFileHandler(@NotNull Class<T> type) {
        this(type, null);
    }

    public @Nullable AutoFileOptions getOptions() {
        return options;
    }

    public @NotNull Class<T> getType() {
        return type;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        FileObject map = new FileObject();
        FileRegistry registry = context.getRegistry();
        for(Field field : CruxReflect.getAllDeclaredFields(object.getClass(), CruxReflect.NON_STATIC(null))){
            boolean isTransient = Modifier.isTransient(field.getModifiers());
            if(isTransient) continue;

            if(options != null && options.testDisabled(field)) continue;
            try{
                boolean x = field.canAccess(object);
                field.setAccessible(true);
                Object obj = field.get(object);
                field.setAccessible(x);
                if(obj == null) continue;

                if(options != null && options.hasManualHandler(field.getName())){
                    FileElement serialized = options.getManualHandler(field.getName()).attemptSerializeToFile(context, obj);
                    if(serialized==null) continue;
                    map.add(field.getName(), serialized);
                    continue;
                }

                FileElement serialized = registry.serializeToFile(obj, context);
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
        for(Field field : CruxReflect.getAllDeclaredFields(type, CruxReflect.NON_STATIC(null))){
            boolean isTransient = Modifier.isTransient(field.getModifiers());
            if(isTransient) continue;
            if(options != null && options.testDisabled(field)) continue;
            String fieldName = field.getName();

            Object found;
            if(options != null && options.hasManualHandler(fieldName)) {
                found = options.getManualHandler(fieldName).deserializeFromFile(context, yamlMap.get(fieldName));
            }else if(options != null && options.hasTypeToken(fieldName)){
                found = registry.deserializeFromFile(options.getTypeToken(fieldName).getType(), yamlMap.get(fieldName));
            }else{
                found = registry.deserializeFromFile(field.getType(), yamlMap.get(fieldName));

                found = CruxObjects.unboxIfNecessary(field.getType(), found, null);
            }
            if(options != null && !options.testIsValid(fieldName, found)) return null;
            fields.put(fieldName, found);
        }
        return CruxReflect.attemptCreation(type, fields);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return type.getSimpleName().toLowerCase();
    }
}
