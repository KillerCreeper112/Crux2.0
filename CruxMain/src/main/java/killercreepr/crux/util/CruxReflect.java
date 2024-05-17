package killercreepr.crux.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CruxReflect {
    public static @NotNull Map<String, Object> getDeclaredFields(@NotNull Object from) {
        Map<String, Object> fields = new LinkedHashMap<>();
        for(Field field : from.getClass().getDeclaredFields()){
            try{
                boolean accessible = field.canAccess(from);
                field.setAccessible(true);
                Object found  = field.get(from);
                field.setAccessible(accessible);
                fields.put(field.getName(), found);
            }catch (IllegalAccessException ignored){}
        }
        return fields;
    }

    public static <T> @Nullable T attemptCreation(@NotNull Class<T> type, @NotNull Map<String, Object> fields){
        Object[] fieldsArray = fields.values().toArray(new Object[0]);
        for(Constructor<?> constructor : type.getConstructors()){
            try{
                T object = (T) constructor.newInstance();
                fields.forEach((key, value) ->{
                    try {
                        Field field = object.getClass().getField(key);
                        boolean x = field.canAccess(object);
                        field.setAccessible(true);
                        field.set(object, value);
                        field.setAccessible(x);
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {}
                });
                return object;
            }catch (InvocationTargetException | InstantiationException |
                    IllegalAccessException | IllegalArgumentException ignored){
                try{
                    T object = (T) constructor.newInstance(fieldsArray);
                    return object;
                }catch (InvocationTargetException | InstantiationException |
                        IllegalAccessException | IllegalArgumentException ignoredAgain){
                }
            }
        }
        return null;
    }
}
