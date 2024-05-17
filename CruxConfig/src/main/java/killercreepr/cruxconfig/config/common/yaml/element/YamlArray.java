package killercreepr.cruxconfig.config.common.yaml.element;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class YamlArray extends YamlElement implements Iterable<YamlElement> {
    protected final List<YamlElement> elements;
    public YamlArray() {
        elements = new ArrayList<>();
    }

    public YamlArray(int capacity) {
        elements = new ArrayList<>(capacity);
    }

    public YamlArray(@NotNull Collection<YamlElement> list) {
        elements = new ArrayList<>(list.size());
        elements.addAll(list);
    }

    public void add(Boolean bool) {
        elements.add(new YamlPrimitive(bool));
    }

    public void add(Number number) {
        elements.add(new YamlPrimitive(number));
    }

    public void add(String string) {
        elements.add(new YamlPrimitive(string));
    }

    public void add(YamlElement element) {
        elements.add(element);
    }

    public void addAll(YamlArray array) {
        elements.addAll(array.elements);
    }

    public YamlElement set(int index, YamlElement element) {
        return elements.set(index, element);
    }

    public boolean remove(YamlElement element) {
        return elements.remove(element);
    }

    public YamlElement remove(int index) {
        return elements.remove(index);
    }

    public boolean contains(YamlElement element) {
        return elements.contains(element);
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public @NotNull Iterator<YamlElement> iterator() {
        return elements.iterator();
    }

    public YamlElement get(int i) {
        return elements.get(i);
    }

    private YamlElement getAsSingleElement() {
        int size = elements.size();
        if (size == 1) {
            return elements.getFirst();
        }
        throw new IllegalStateException("Array must have size 1, but has size " + size);
    }

    @Override
    public Object getAsObject() {
        return elements;
    }

    @Override
    public Number getAsNumber() {
        return getAsSingleElement().getAsNumber();
    }

    @Override
    public String getAsString() {
        return getAsSingleElement().getAsString();
    }

    @Override
    public double getAsDouble() {
        return getAsSingleElement().getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return getAsSingleElement().getAsFloat();
    }

    @Override
    public long getAsLong() {
        return getAsSingleElement().getAsLong();
    }

    @Override
    public int getAsInt() {
        return getAsSingleElement().getAsInt();
    }

    @Override
    public byte getAsByte() {
        return getAsSingleElement().getAsByte();
    }

    @Override
    public short getAsShort() {
        return getAsSingleElement().getAsShort();
    }

    @Override
    public boolean getAsBoolean() {
        return getAsSingleElement().getAsBoolean();
    }

    public List<YamlElement> asList() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof YamlArray && ((YamlArray) o).elements.equals(elements));
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}
