package killercreepr.cruxconfig.config.common.element;

import com.google.gson.JsonObject;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class FileObject extends FileElement implements Iterable<Map.Entry<String, FileElement>>, DataObject {
    public static @NotNull FileObject fromJson(@NotNull JsonObject e){
        FileObject obj = new FileObject();
        e.asMap().forEach((key, value) -> obj.add(key, FileElement.fromJson(value)));
        return obj;
    }

    public static @NotNull FileObject fromYaml(@NotNull YamlObject e){
        FileObject obj = new FileObject();
        e.asMap().forEach((key, value) -> obj.add(key, FileElement.fromYaml(value)));
        return obj;
    }

    protected final Map<String, FileElement> members = new LinkedHashMap<>();
    public FileObject add(String property, FileElement value) {
        if(value==null) return this;
        members.put(property, value);
        return this;
    }

    public FileObject addAll(@NotNull FileObject object) {
        addAll(object.asMap()); return this;
    }

    public FileObject addAll(@NotNull Map<String, FileElement> map) {
        members.putAll(map); return this;
    }

    public FileElement remove(String property) {
        return members.remove(property);
    }

    public FileObject addProperty(String property, String value) {
        if(value==null) return this;
        add(property, new FilePrimitive(value)); return this;
    }

    public FileObject addProperty(String property, Number value) {
        if(value==null) return this;
        add(property, new FilePrimitive(value)); return this;
    }

    public FileObject addProperty(String property, Boolean value) {
        if(value==null) return this;
        add(property, new FilePrimitive(value)); return this;
    }

    public Set<Map.Entry<String, FileElement>> entrySet() {
        return members.entrySet();
    }

    public Set<String> keySet() {
        return members.keySet();
    }

    public int size() {
        return members.size();
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public boolean has(String memberName) {
        return members.containsKey(memberName);
    }

    public FileElement get(String memberName) {
        return members.get(memberName);
    }

    @Override
    public void put(String key, DataNode value) {
        members.put(key, FileElement.fromDataNode(value));
    }

    @Override
    public void forEachDataPair(BiConsumer<String, DataNode> consumer) {
        members.forEach(consumer);
    }

    /**
     * Searches, in order, for the member names. It will return the first YamlElement that
     * comes back not-null.
     */
    public FileElement search(String... memberNames){
        for(String s : memberNames){
            FileElement e = get(s);
            if(e != null) return e;
        }
        return null;
    }

    public FileElement getOrDefault(String memberName, FileElement defaultValue){
        return members.getOrDefault(memberName, defaultValue);
    }

    public <T> T searchForObject(@NotNull Class<T> type, String... memberNames){
        return searchForObject(type, null, memberNames);
    }

    public <T> T searchForObject(@NotNull Class<T> type, T defaultValue, String... memberNames){
        Object o = searchOrDefaultObject(type, defaultValue, memberNames);
        if(o == null) return defaultValue;
        if(type.isAssignableFrom(o.getClass())) return type.cast(o);
        return defaultValue;
    }


    public <T> T getObject(@NotNull Class<T> type, String memberName){
        return getObject(type, memberName, null);
    }

    public <T> T getObject(@NotNull Class<T> type, String memberName, T defaultValue){
        Object o = getOrDefaultObject(type, memberName, defaultValue);
        if(o == null) return defaultValue;
        if(type.isAssignableFrom(o.getClass())) return type.cast(o);
        return defaultValue;
    }

    public <T> T getObject(String memberName){
        return getOrDefaultObject(memberName, null);
    }

    public <T> T searchOrDefaultObject(T defaultValue, String... memberNames){
        FileElement e = search(memberNames);
        if(e==null) return defaultValue;
        return getOrDefault(e, defaultValue);
    }

    public <T> T searchOrDefaultObject(@NotNull Class<T> type, T defaultValue, String... memberNames){
        FileElement e = search(memberNames);
        if(e==null) return defaultValue;
        return getOrDefault(type, e, defaultValue);
    }

    public <T> T getOrDefault(@NotNull FileElement e, T defaultValue){
        Object o = e.getAsObject();
        if(o==null) return defaultValue;

        if(o instanceof Number n){
            if(defaultValue instanceof Integer){
                o = n.intValue();
            }else if(defaultValue instanceof Float){
                o = n.floatValue();
            }else if(defaultValue instanceof Double){
                o = n.doubleValue();
            }else if(defaultValue instanceof Long){
                o = n.longValue();
            }else if(defaultValue instanceof Short){
                o = n.shortValue();
            }else if(defaultValue instanceof Byte){
                o = n.byteValue();
            }
        }

        try{
            return (T) o;
        }catch (ClassCastException ignored){ return defaultValue; }
    }

    public <T> T getOrDefault(@NotNull Class<T> type, @NotNull FileElement e, T defaultValue){
        Object o = e.getAsObject();
        if(o==null) return defaultValue;
        if(o instanceof Number n){
            if (Integer.class.isAssignableFrom(type)) {
                o = n.intValue();
            } else if (Float.class.isAssignableFrom(type)) {
                o = n.floatValue();
            } else if (Double.class.isAssignableFrom(type)) {
                o = n.doubleValue();
            } else if (Long.class.isAssignableFrom(type)) {
                o = n.longValue();
            } else if (Short.class.isAssignableFrom(type)) {
                o = n.shortValue();
            } else if (Byte.class.isAssignableFrom(type)) {
                o = n.byteValue();
            }
        }

        try{
            return (T) o;
        }catch (ClassCastException ignored){ return defaultValue; }
    }

    public <T> T getOrDefaultObject(@NotNull Class<T> type, String memberName, T defaultValue){
        FileElement e = get(memberName);
        if(e==null) return defaultValue;

        return getOrDefault(type, e, defaultValue);
    }

    public <T> T getOrDefaultObject(String memberName, T defaultValue){
        FileElement e = get(memberName);
        if(e==null) return defaultValue;

        return getOrDefault(e, defaultValue);

        /*todo maybe? if (defaultValue instanceof Number && o instanceof Number valueNumber) {
            // If both defaultValue and o are numeric types, perform conversion if necessary

            if (defaultValue instanceof Float) {
                o = valueNumber.floatValue();
            } else if (defaultValue instanceof Long) {
                o = valueNumber.longValue();
            } else if (defaultValue instanceof Integer) {
                o = valueNumber.intValue();
            }else if(defaultValue instanceof Double){
                o = valueNumber.doubleValue();
            }else if(defaultValue instanceof Short){
                o = valueNumber.shortValue();
            }else if(defaultValue instanceof Byte){
                o = valueNumber.byteValue();
            }
        }*/
    }

    public FilePrimitive getAsYamlPrimitive(String memberName) {
        return (FilePrimitive) members.get(memberName);
    }

    public FileArray getAsYamlArray(String memberName) {
        return (FileArray) members.get(memberName);
    }

    public FileObject getAsYamlObject(String memberName) {
        return (FileObject) members.get(memberName);
    }

    public Map<String, FileElement> asMap() {
        return members;
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject obj = new JsonObject();
        members.forEach((key, value) -> obj.add(key, value.toJson()));
        return obj;
    }

    @Override
    public @NotNull YamlObject toYaml() {
        YamlObject obj = new YamlObject();
        members.forEach((key, value) -> obj.add(key, value.toYaml()));
        return obj;
    }

    @Override
    public Object getAsObject() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof FileObject
                && ((FileObject) o).members.equals(members));
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }

    @Override
    public String toString() {
        return "FileObject{" + members + "}";
    }

    public void forEach(BiConsumer<String, FileElement> action) {
        members.forEach(action);
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<String, FileElement>> iterator() {
        return members.entrySet().iterator();
    }
}
