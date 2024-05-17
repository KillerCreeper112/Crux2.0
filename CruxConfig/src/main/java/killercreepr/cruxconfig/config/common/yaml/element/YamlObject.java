package killercreepr.cruxconfig.config.common.yaml.element;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class YamlObject extends YamlElement {
    protected final Map<String, YamlElement> members = new LinkedHashMap<>();
    public void add(String property, YamlElement value) {
        members.put(property, value);
    }

    public void addAll(@NotNull YamlObject object) {
        addAll(object.asMap());
    }

    public void addAll(@NotNull Map<String, YamlElement> map) {
        members.putAll(map);
    }

    public YamlElement remove(String property) {
        return members.remove(property);
    }

    public void addProperty(String property, String value) {
        add(property, new YamlPrimitive(value));
    }

    public void addProperty(String property, Number value) {
        add(property, new YamlPrimitive(value));
    }

    public void addProperty(String property, Boolean value) {
        add(property, new YamlPrimitive(value));
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
