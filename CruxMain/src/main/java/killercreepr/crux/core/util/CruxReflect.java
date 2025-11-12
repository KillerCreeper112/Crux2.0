package killercreepr.crux.core.util;

import org.bukkit.Bukkit;
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

    public static List<Class<?>> getClassInheritanceChain(Class<?> from) {
        List<Class<?>> chain = new ArrayList<>();
        Class<?> current = from;
        while (current != null) {
            chain.add(current);
            current = current.getSuperclass();
            if(current == Object.class) break;
        }
        return chain;
    }

    public static int calculateClassCloseness(List<Class<?>> fromInheritanceChain, Class<?> clazz) {
        // Check if the class is part of the `from` class's inheritance chain
        for (int i = 0; i < fromInheritanceChain.size(); i++) {
            if (fromInheritanceChain.get(i).equals(clazz)) {
                return i; // Return the index as the "distance" from `from`
            }
        }

        // If no relationship, return a large number to push it to the end
        return Integer.MAX_VALUE;
    }

    /*private static int calculateClassCloseness(Class<?> from, Class<?> clazz) {
        if (clazz.equals(from)) {
            return 0; // Same class, no distance
        }

        // Distance based on inheritance hierarchy
        int distance = 0;
        Class<?> current = clazz;
        while (current != null) {
            if (current.equals(from)) {
                return distance; // Found the class in the hierarchy
            }
            current = current.getSuperclass();
            distance++;
        }

        // If no relationship, return a large number to push it to the end
        return Integer.MAX_VALUE;
    }*/

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
        return getParsedDeclaredFields(from.getClass(), from, filter);
    }

    public static @NotNull LinkedHashMap<String, Object> getParsedDeclaredFields(@NotNull Class<?> clazz, @NotNull Object from, @Nullable Predicate<Field> filter) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        for(Field field : clazz.getDeclaredFields()){
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

    public static @Nullable Field getDeclaredField(@NotNull Class<?> type, @NotNull String id){
        Class<?> superClass = type.getSuperclass();
        while (superClass != null) {
            try{
                return superClass.getDeclaredField(id);
            }catch (NoSuchFieldException ignored){}

            superClass = superClass.getSuperclass();
        }
        return null;
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


    public static <T> @Nullable T attemptCreation(@NotNull Class<T> type, @NotNull Map<String, Object> fields) {
        try {
            // Try to find a no-args constructor first
            Constructor<T> noArgsConstructor = null;
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    //noinspection unchecked
                    noArgsConstructor = (Constructor<T>) constructor;
                    break;
                }
            }

            T instance = null;
            if (noArgsConstructor != null) {
                noArgsConstructor.setAccessible(true);
                instance = noArgsConstructor.newInstance();
            } else {
                // Try to find a constructor whose parameter count matches the map size
                for (Constructor<?> constructor : type.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() == fields.size()) {
                        try {
                            constructor.setAccessible(true);
                            Object[] args = fields.values().toArray(new Object[0]);
                            //noinspection unchecked
                            instance = (T) constructor.newInstance(args);
                            break;
                        } catch (Exception ignored) {}
                    }
                }
            }
            // Now set fields
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();

                try {
                    Field field = findField(type, name);
                    if(field == null) continue;

                    boolean wasAccessible = field.canAccess(instance);
                    field.setAccessible(true);
                    try {
                        field.set(instance, value);
                    } catch(IllegalArgumentException | NullPointerException e) {
                    }finally {
                        field.setAccessible(wasAccessible);
                    }
                } catch (ReflectiveOperationException ignored) {
                }
            }

            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    public static @Nullable Field findField(Class<?> type, String name) {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
