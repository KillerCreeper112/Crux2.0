package killercreepr.cruxconfig.config.common.base;

import com.google.gson.JsonElement;
import com.google.gson.internal.LazilyParsedNumber;
import killercreepr.crux.core.util.CruxObjects;
import killercreepr.crux.core.util.CruxReflect;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.base.registry.FileObjectHandlerRegistry;
import killercreepr.cruxconfig.config.common.base.registry.FileParsedObjectRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Represents a registry specifically developed to handle YAML syntax.
 * Keep in mind, this functions differently than Crux's JSON framework.
 * It was made primarily for saving and extracting config values. Therefor meaning that the
 * software knows exactly what classes it needs to serialize and what classes it needs to deserialize.
 */
public class BaseFileRegistry implements FileRegistry {
    public final FileObjectHandlerRegistry HANDLER_REGISTRY = new FileObjectHandlerRegistry(new HashMap<>(),this);
    public final FileParsedObjectRegistry PARSED_OBJECT_HANDLERS = new FileParsedObjectRegistry();

    @Override
    public <T extends FileObjectHandler<?>> void registerFileHandler(@NotNull Class<?> clazz, @NotNull T handler) {
        HANDLER_REGISTRY.register(clazz, handler);
    }

    @Override
    public @NotNull FileObjectHandlerRegistry getHandlerRegistry() {
        return HANDLER_REGISTRY;
    }

    @Override
    public @NotNull FileParsedObjectRegistry getParsedObjectRegistry() {
        return PARSED_OBJECT_HANDLERS;
    }

    public @Nullable FileObjectHandler<?> getContainerHandler(@NotNull Class<?> clazz){
        return HANDLER_REGISTRY.get(clazz);
    }

    public @NotNull Collection<FileObjectHandler<?>> findPotentialHandlers(@NotNull Class<?> from) {
        List<FileObjectHandler<?>> handlerList = new ArrayList<>();

        // Collect matching handlers
        for (Map.Entry<Class<?>, FileObjectHandler<?>> entry : HANDLER_REGISTRY.entrySet()) {
            Class<?> clazz = entry.getKey();
            if (from.isAssignableFrom(clazz)) {
                handlerList.add(entry.getValue());
            }
        }

        List<Class<?>> fromInheritanceChain = CruxReflect.getClassInheritanceChain(from);

        // Sort the handlers based on their closeness to `from`
        handlerList.sort(Comparator.comparingInt(handler -> CruxReflect.calculateClassCloseness(fromInheritanceChain, handler.getClass())));

        return handlerList;
    }

    /*public @NotNull Collection<FileObjectHandler<?>> findPotentialHandlers(@NotNull Class<?> from){
        Collection<FileObjectHandler<?>> handlers = new HashSet<>();
        for(Map.Entry<Class<?>, FileObjectHandler<?>> entry : HANDLER_REGISTRY.entrySet()){
            Class<?> clazz = entry.getKey();
            if(from.isAssignableFrom(clazz)){
                handlers.add(entry.getValue());
            }
        }
        return handlers;
    }*/

    public @Nullable FileObjectHandler<?> findObjectHandler(@NotNull Class<?> from){
        //first check if there are any exact classes
        Collection<Map.Entry<Class<?>, FileObjectHandler<?>>> check = new HashSet<>();
        for(Map.Entry<Class<?>, FileObjectHandler<?>> entry : HANDLER_REGISTRY.entrySet()){
            Class<?> clazz = entry.getKey();
            if(clazz.equals(from)){
                return entry.getValue();
            }
            check.add(entry);
        }

        //then check if any classes are assignable
        for(Map.Entry<Class<?>, FileObjectHandler<?>> entry : check){
            Class<?> clazz = entry.getKey();
            if(clazz.isAssignableFrom(from)){
                return entry.getValue();
            }
        }
        return null;
    }

    public @Nullable FileElement serialize(@NotNull Object object, @NotNull FileContext<?> context){
        /*FileObjectHandler<?> first = HANDLER_REGISTRY.get(object.getClass());
        if(first != null) return first.attemptSerializeToFile(context, first);*/
        FileObjectHandler<?> handler = findObjectHandler(object.getClass());
        if(handler == null) return null;
        return handler.attemptSerializeToFile(context, object);
    }

    public static boolean isSubtypeOfCollection(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?> rawClass) {
                return Collection.class.isAssignableFrom(rawClass);
            }
        }
        return false;
    }
    public static boolean isSubtypeOfMap(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?> rawClass) {
                return Map.class.isAssignableFrom(rawClass);
            }
        }
        return false;
    }

    @Contract("_, _, null -> null")
    public @Nullable Object parseObjectFromHandlers(@NotNull FileElement from, @NotNull FileContext<?> context, @Nullable Object object){
        if(object==null) return null;
        return PARSED_OBJECT_HANDLERS.parse(from, context, object);
    }

    public @Nullable Object deserializeObjectRaw(@NotNull Type type, @NotNull FileElement from, @NotNull FileContext<?> context){
        if(isSubtypeOfCollection(type)){
            Type[] args = CruxReflect.getTypeArguments(type);
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            return deserializeObjectCollection(rawType, args[0], from);
        }else if(isSubtypeOfMap(type)){
            Type[] args = CruxReflect.getTypeArguments(type);
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            return deserializeObjectMap(rawType, args[0], args[1], from.getAsFileObject());
        }

        if(!(type instanceof Class<?> clazz)){
            throw new UnsupportedOperationException(type + " is not a class instance!");
        }


        //optimize
        FileObjectHandler<?> firstHandler = HANDLER_REGISTRY.get(clazz);
        if(firstHandler != null){
            Object o = firstHandler.deserializeFromFile(context, from);
            if(o!=null){
                return formatObject(clazz, deserializeObject(o));
            }
            return null;
        }

        for(FileObjectHandler<?> handler : findPotentialHandlers(clazz)){
            Object o = handler.deserializeFromFile(context, from);
            if(o!=null){
                return formatObject(clazz, deserializeObject(o));
            }
        }
        Object object = deserializeObject(from.getAsObject());
        return formatObject(clazz, object);
    }

    /**
     * This should be used
     */
    public @Nullable Object deserializeObject(@NotNull Type type, @NotNull FileElement from, @NotNull FileContext<?> context){
        if(type instanceof Class<?> clazz){
            if(Double.class.isAssignableFrom(clazz)){
                return from.getAsNumber().doubleValue();
            }
            if(Float.class.isAssignableFrom(clazz)){
                return from.getAsNumber().floatValue();
            }
            if(Short.class.isAssignableFrom(clazz)){
                return from.getAsNumber().shortValue();
            }
            if(Long.class.isAssignableFrom(clazz)){
                return from.getAsNumber().longValue();
            }
            if(Integer.class.isAssignableFrom(clazz)){
                return from.getAsNumber().intValue();
            }
            if(Byte.class.isAssignableFrom(clazz)){
                return from.getAsNumber().byteValue();
            }
            if(String.class.isAssignableFrom(clazz)){
                return from.getAsString();
            }
            if(Character.class.isAssignableFrom(clazz)){
                return from.getAsString().charAt(0);
            }
            if(Boolean.class.isAssignableFrom(clazz)){
                return from.getAsBoolean();
            }
        }

        Object object = deserializeObjectRaw(type, from, context);
        return parseObjectFromHandlers(from, context, object);
    }

    public @Nullable Object deserializeObject(@NotNull Type type, @Nullable FileElement from){
        if(from==null) return null;
        return deserializeObject(type, from, new FileContext<>(this));
    }

    public @Nullable Object deserializeObjectCollection(@NotNull Class<?> collectionClazz, @NotNull Type firstType,
                                                 @NotNull FileElement from){
        Object createdMap = CruxReflect.attemptCreation(collectionClazz);
        if(createdMap == null){
            if(Set.class.isAssignableFrom(collectionClazz)){
                createdMap = new HashSet<>();
            }else createdMap = new ArrayList<>();
        }

        Collection<Object> map = (Collection<Object>) createdMap;
        if(from.isFileArray()){
            from.getAsFileArray().forEach((value) ->{
                Object parsedValue = deserializeObject(firstType, value);
                if(parsedValue==null) return;
                map.add(parsedValue);
            });
        }else{
            Object parsedValue = deserializeObject(firstType, from);
            if(parsedValue!=null) map.add(parsedValue);
        }
        return createdMap;
    }

    public @Nullable Object deserializeObjectMap(@NotNull Class<?> mapClazz, @NotNull Type firstType, @NotNull Type secondType,
                                                 @NotNull FileObject from){
        Object createdMap = CruxReflect.attemptCreation(mapClazz);
        if(createdMap == null) createdMap = new HashMap<>();

        Map<Object, Object> map = (Map<Object, Object>) createdMap;
        from.asMap().forEach((key, value) ->{
            Object parsedValue = deserializeObject(secondType, value);
            if(parsedValue == null) return;
            Object parsedKey = deserializeObject(firstType, new FilePrimitive(key));
            /*if(firstType == Integer.class){
                parsedKey = Integer.parseInt(key);
            }else if(firstType == Double.class){
                parsedKey = Double.parseDouble(key);
            }else if(firstType == Float.class){
                parsedKey = Float.parseFloat(key);
            }else if(firstType == Short.class){
                parsedKey = Short.parseShort(key);
            }else if(firstType == Long.class){
                parsedKey = Long.parseLong(key);
            }else if(firstType == Boolean.class){
                parsedKey = Boolean.parseBoolean(key);
            }else if(firstType == Character.class){
                parsedKey = key.charAt(0);
            }else{
                parsedKey = deserializeObject(firstType, new FilePrimitive(key));
            }*/
            map.put(parsedKey, parsedValue);
        });
        return createdMap;
    }

    public @NotNull Object formatObject(@NotNull Class<?> clazz, @NotNull Object object){
        if(clazz.isAssignableFrom(Collection.class) && object instanceof Map<?,?> map){
            return new ArrayList<>(map.values());
        }
        return object;
    }

    public <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @NotNull FileElement from, @NotNull FileContext<?> context){
        Object object = deserializeObject(clazz, from, context);
        if(object==null) return null;
        return CruxObjects.castOrThrow(clazz, object);
    }

    @Override
    public <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement from){
        Object object = deserializeObject(clazz, from);
        if(object==null) return null;
        return CruxObjects.castOrThrow(clazz, object);
    }

    public @NotNull Object deserializeObject(@NotNull Object o){
        if(o instanceof Collection<?> l){
            return deserializeCollection(l);
        }
        if(o.getClass().isArray()){
            return deserializeCollection(Arrays.stream(((Object[]) o)).toList());
        }
        if(o instanceof Map<?,?> l) return deserializeMap(l);
        if(o instanceof FileElement g) return deserializeObject(g.getAsObject());
        return o;
    }

    public @NotNull FileElement serializeObject(@NotNull Object o, @NotNull FileContext<?> context){
        if(o instanceof FileElement d) return d;
        if(o instanceof YamlElement d) return FileElement.fromYaml(d);
        if(o instanceof JsonElement d) return FileElement.fromJson(d);
        if(o instanceof String s) return new FilePrimitive(s);
        if(o instanceof Character s) return new FilePrimitive(s.toString());
        if(o instanceof Number s) return new FilePrimitive(s);
        if(o instanceof Boolean s) return new FilePrimitive(s);
        if(o instanceof Collection<?> l) return serializeCollection(l, context);
        if(o.getClass().isArray()){
            return serializeCollection(Arrays.stream(((Object[]) o)).toList(), context);
        }
        if(o instanceof Map<?,?> l) return serializeMap(l, context);
        FileElement serialized = serialize(o, context);
        if(serialized != null) return serialized;
        throw new UnsupportedOperationException(o.getClass().getSimpleName() + " not able to be serialized.");
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull Object object) {
        return serializeObject(object, new FileContext<>(this));
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull Object object, @NotNull FileContext<?> context) {
        return serializeObject(object, context);
    }

    @Override
    public <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o) {
        if(o==null) return null;
        Object object = deserializeObject(type, o);
        return (T) object;
    }

    @Override
    public <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o, @NotNull FileContext<?> context) {
        if(o==null) return null;
        Object object = deserializeObject(type, o, context);
        return (T) object;
    }

    @Override
    public @NotNull Object deserializeObjectFromFile(@NotNull FileElement o) {
        return deserializeObject(o);
    }

    public @NotNull Collection<Object> deserializeCollection(@NotNull Collection<?> list){
        Collection<Object> array = new ArrayList<>(list.size());
        for(Object o : list){
            array.add(deserializeObject(o));
        }
        return array;
    }

    public @NotNull Map<Object, Object> deserializeMap(@NotNull Map<?, ?> list){
        Map<Object, Object> array = new HashMap<>();
        for(Map.Entry<?, ?> entry : list.entrySet()){
            array.put(deserializeObject(entry.getKey()), deserializeObject(entry.getValue()));
        }
        return array;
    }

    public @NotNull FileArray serializeCollection(@NotNull Collection<?> list, @NotNull FileContext<?> context){
        FileArray array = new FileArray(list.size());
        for(Object o : list){
            array.add(serializeObject(o, context));
        }
        return array;
    }

    public @NotNull FileObject serializeMap(@NotNull Map<?, ?> list, @NotNull FileContext<?> context){
        FileObject array = new FileObject();
        for(Map.Entry<?, ?> entry : list.entrySet()){
            FileElement serializedKey = serializeObject(entry.getKey(), context);
            if(!(serializedKey instanceof FilePrimitive prim)){
                throw new UnsupportedOperationException(entry.getKey() + " was not serialized into a primitive element! " +
                    entry.getKey().getClass().getSimpleName() + " Maps require the key element to be serialized into a string. Got " + serializedKey);
            }

            array.add(prim.getAsString(), serializeObject(entry.getValue(), context));
        }
        return array;
    }

}
