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

    public static Type[] getTypeArguments(@NotNull Type type){
        if(type instanceof ParameterizedType t){
            return t.getActualTypeArguments();
        }
        return new Type[0];
    }

    public static <T extends Map<?, ?>> Class<?> attemptGetFirstMapClass(Class<T> type){
        try{
            return getFirstMapClass(type);
        }catch (Exception e){ return null; }
    }

    public static <T extends Map<?, ?>> Class<?> getFirstMapClass(Class<T> type) {
        Type genericSuperclass = type.getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType) {
            Type[] typeArgs = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (typeArgs.length > 0) {
                Type firstTypeArg = typeArgs[0];
                if (firstTypeArg instanceof Class) {
                    return (Class<?>) firstTypeArg;
                } else if (firstTypeArg instanceof ParameterizedType) {
                    // If the type argument itself is parameterized (e.g., Map<K, V>), you may need further resolution.
                    // You may implement the resolveClass method to handle this case.
                    // return resolveClass((ParameterizedType) firstTypeArg);
                    throw new IllegalArgumentException("Nested parameterized types are not supported yet.");
                } else if (firstTypeArg instanceof WildcardType) {
                    // Handle wildcard types if needed.
                    throw new IllegalArgumentException("Wildcard types are not supported yet.");
                } else {
                    throw new IllegalArgumentException("Unsupported type argument: " + firstTypeArg.getTypeName());
                }
            } else {
                throw new IllegalArgumentException("No type arguments found for the superclass.");
            }
        } else {
            throw new IllegalArgumentException("The superclass is not parameterized.");
        }
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

    public static @NotNull Field[] getAllDeclaredFields(@NotNull Class<?> type, @Nullable Predicate<Field> filter) {
        Set<Field> fields = new LinkedHashSet<>();

        // Add fields from all superclasses recursively
        Class<?> superClass = type.getSuperclass();
        List<Class<?>> superClasses = new ArrayList<>();
        while (superClass != null) {
            superClasses.add(superClass);
            superClass = superClass.getSuperclass();
        }
        Collections.reverse(superClasses);

        for(Class<?> clazz : superClasses){
            addDeclaredFields(clazz, fields, filter);
        }

        // Add fields from the current class
        addDeclaredFields(type, fields, filter);

        return fields.toArray(Field[]::new);
    }

    private static void addDeclaredFields(@NotNull Class<?> type, @NotNull Collection<Field> fields, @Nullable Predicate<Field> filter) {
        for (Field field : type.getDeclaredFields()) {
            if (filter == null || filter.test(field)) {
                fields.add(field);
            }
        }
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
