package killercreepr.cruxconfig.config.common.yaml.element;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class YamlObject extends YamlElement {
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
    public YamlElement getOrDefault(String memberName, YamlElement defaultValue){
        return members.getOrDefault(memberName, defaultValue);
    }

    public <T> T getObject(String memberName){
        return getOrDefaultObject(memberName, null);
    }
    public <T> T getOrDefaultObject(String memberName, T defaultValue){
        YamlElement e = get(memberName);
        if(e==null) return defaultValue;
        Object o = e.getAsObject();
        if(o==null) return defaultValue;
        try{
            return (T) o;
        }catch (ClassCastException ignored){ return defaultValue; }
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

    public void forEach(BiConsumer<String, YamlElement> action) {
        members.forEach(action);
    }
}
