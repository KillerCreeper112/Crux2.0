package killercreepr.cruxconfig.config.common.yaml.element;

import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class YamlObject extends YamlElement implements Iterable<Map.Entry<String, YamlElement>>, DataObject {
    protected final Map<String, YamlElement> members = new LinkedHashMap<>();
    public YamlObject add(String property, YamlElement value) {
        members.put(property, value);
        return this;
    }

    public YamlObject addAll(@NotNull YamlObject object) {
        addAll(object.asMap()); return this;
    }

    public YamlObject addAll(@NotNull Map<String, YamlElement> map) {
        members.putAll(map); return this;
    }

    public YamlElement remove(String property) {
        return members.remove(property);
    }

    public YamlObject addProperty(String property, String value) {
        add(property, new YamlPrimitive(value)); return this;
    }

    public YamlObject addProperty(String property, Number value) {
        add(property, new YamlPrimitive(value)); return this;
    }

    public YamlObject addProperty(String property, Boolean value) {
        add(property, new YamlPrimitive(value)); return this;
    }

    public Set<Map.Entry<String, YamlElement>> entrySet() {
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

    public YamlElement get(String memberName) {
        return members.get(memberName);
    }

    @Override
    public void put(String key, DataNode value) {
        members.put(key, YamlElement.fromDataNode(value));
    }

    @Override
    public void forEachDataPair(BiConsumer<String, DataNode> consumer) {
        members.forEach(consumer);
    }

    /**
     * Searches, in order, for the member names. It will return the first YamlElement that
     * comes back not-null.
     */
    public YamlElement search(String... memberNames){
        for(String s : memberNames){
            YamlElement e = get(s);
            if(e != null) return e;
        }
        return null;
    }

    public YamlElement getOrDefault(String memberName, YamlElement defaultValue){
        return members.getOrDefault(memberName, defaultValue);
    }

    public <T> T searchForObject(@NotNull Class<T> type, String... memberNames){
        return searchForObject(type, null, memberNames);
    }

    public <T> T searchForObject(@NotNull Class<T> type, T defaultValue, String... memberNames){
        Object o = searchOrDefaultObject(defaultValue, memberNames);
        if(o == null) return defaultValue;
        if(type.isAssignableFrom(o.getClass())) return type.cast(o);
        return defaultValue;
    }


    public <T> T getObject(@NotNull Class<T> type, String memberName){
        return getObject(type, memberName, null);
    }

    public <T> T getObject(@NotNull Class<T> type, String memberName, T defaultValue){
        Object o = getOrDefaultObject(memberName, defaultValue);
        if(o == null) return defaultValue;
        if(type.isAssignableFrom(o.getClass())) return type.cast(o);
        return defaultValue;
    }

    public <T> T getObject(String memberName){
        return getOrDefaultObject(memberName, null);
    }

    public <T> T searchOrDefaultObject(T defaultValue, String... memberNames){
        YamlElement e = search(memberNames);
        if(e==null) return defaultValue;
        return getOrDefault(e, defaultValue);
    }

    public <T> T getOrDefault(@NotNull YamlElement e, T defaultValue){
        Object o = e.getAsObject();
        if(o==null) return defaultValue;

        try{
            return (T) o;
        }catch (ClassCastException ignored){ return defaultValue; }
    }

    public <T> T getOrDefaultObject(String memberName, T defaultValue){
        YamlElement e = get(memberName);
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

    public YamlPrimitive getAsYamlPrimitive(String memberName) {
        return (YamlPrimitive) members.get(memberName);
    }

    public YamlArray getAsYamlArray(String memberName) {
        return (YamlArray) members.get(memberName);
    }

    public YamlObject getAsYamlObject(String memberName) {
        return (YamlObject) members.get(memberName);
    }

    public Map<String, YamlElement> asMap() {
        return members;
    }

    @Override
    public Object getAsObject() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof YamlObject
                && ((YamlObject) o).members.equals(members));
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }

    @Override
    public String toString() {
        return "YamlObject{" + members + "}";
    }

    public void forEach(BiConsumer<String, YamlElement> action) {
        members.forEach(action);
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<String, YamlElement>> iterator() {
        return members.entrySet().iterator();
    }
}
