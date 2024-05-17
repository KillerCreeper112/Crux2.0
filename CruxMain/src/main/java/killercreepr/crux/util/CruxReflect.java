package killercreepr.crux.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;

public class CruxReflect {
    public static @NotNull Predicate<Field> NON_STATIC(@Nullable Predicate<Field> filter){
        return field ->{
            if(filter != null && !filter.test(field)) return false;
            return !Modifier.isStatic(field.getModifiers());
        };
    }

    public static <T extends Map<?, ?>> Class<?> getFirstMapClass(Class<T> type){
        Type[] typeArgs = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        return resolveClass(typeArgs[0]);
    }

    public static Class<?> resolveClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType t) {
            return (Class<?>) t.getRawType();
        } else {
            throw new IllegalArgumentException("Type cannot be resolved: " + type);
        }
    }

    public static @NotNull LinkedHashMap<String, Object> getNonStaticParsedDeclaredFields(@NotNull Object from, @Nullable Predicate<Field> filter){
        return getParsedDeclaredFields(from, NON_STATIC(filter));
    }

    public static @NotNull LinkedHashMap<String, Object> getNonStaticParsedDeclaredFields(@NotNull Object from){
        return getNonStaticParsedDeclaredFields(from, null);
    }

    public static @NotNull LinkedHashMap<String, Object> getParsedDeclaredFields(@NotNull Object from){
        return getParsedDeclaredFields(from, null);
    }

    public static @NotNull LinkedHashMap<String, Object> getParsedDeclaredFields(@NotNull Object from, @Nullable Predicate<Field> filter) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        for(Field field : from.getClass().getDeclaredFields()){
            if(filter != null && !filter.test(field)) continue;
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

    public static @NotNull Field[] getNonStaticDeclaredFields(@NotNull Class<?> type){
        return getNonStaticDeclaredFields(type, null);
    }

    public static @NotNull Field[] getNonStaticDeclaredFields(@NotNull Class<?> type, @Nullable Predicate<Field> filter){
        return getDeclaredFields(type, NON_STATIC(filter));
    }

    public static @NotNull Field[] getDeclaredFields(@NotNull Class<?> type, @Nullable Predicate<Field> filter){
        Collection<Field> list = new ArrayList<>();
        for(Field field : type.getDeclaredFields()){
            if(filter != null && !filter.test(field)) continue;
            list.add(field);
        }
        return list.toArray(new Field[0]);
    }
    public static <T> @Nullable T attemptCreation(@NotNull Class<T> type){
        return attemptCreation(type, new HashMap<>());
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
