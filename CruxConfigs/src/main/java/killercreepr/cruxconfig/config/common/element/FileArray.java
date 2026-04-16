package killercreepr.cruxconfig.config.common.element;

import com.google.gson.JsonArray;
import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.ArrayDataNode;
import killercreepr.cruxconfig.config.common.yaml.element.YamlArray;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class FileArray extends FileElement implements Iterable<FileElement>, DataArray {
    public static @NotNull FileArray fromJson(@NotNull JsonArray json){
        FileArray a = new FileArray();
        json.forEach(value -> a.add(FileElement.fromJson(value)));
        return a;
    }

    public static @NotNull FileArray fromYaml(@NotNull YamlArray json){
        FileArray a = new FileArray();
        json.forEach(value -> a.add(FileElement.fromYaml(value)));
        return a;
    }
    public static @NotNull FileArray fromDataNode(@NotNull DataArray json){
        FileArray a = new FileArray();
        json.forEachDataNode(value -> a.add(FileElement.fromDataNode(value)));
        return a;
    }

    protected final List<FileElement> elements;
    public FileArray() {
        elements = new ArrayList<>();
    }

    public FileArray(int capacity) {
        elements = new ArrayList<>(capacity);
    }

    public FileArray(@NotNull Collection<FileElement> list) {
        elements = new ArrayList<>(list.size());
        elements.addAll(list);
    }

    public void add(Boolean bool) {
        elements.add(new FilePrimitive(bool));
    }

    public void add(Number number) {
        elements.add(new FilePrimitive(number));
    }

    public void add(String string) {
        elements.add(new FilePrimitive(string));
    }

    public void add(FileElement element) {
        elements.add(element);
    }

    public void addAll(FileArray array) {
        elements.addAll(array.elements);
    }

    public FileElement set(int index, FileElement element) {
        return elements.set(index, element);
    }

    public boolean remove(FileElement element) {
        return elements.remove(element);
    }

    public FileElement remove(int index) {
        return elements.remove(index);
    }

    public boolean contains(FileElement element) {
        return elements.contains(element);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public void forEachDataNode(Consumer<DataNode> consumer) {
        forEach(consumer);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public @NotNull Iterator<FileElement> iterator() {
        return elements.iterator();
    }

    public FileElement get(int i) {
        return elements.get(i);
    }

    @Override
    public void add(DataNode value) {
        elements.add(FileElement.fromDataNode(value));
    }

    private FileElement getAsSingleElement() {
        int size = elements.size();
        if (size == 1) {
            return elements.getFirst();
        }
        throw new IllegalStateException("Array must have size 1, but has size " + size);
    }

    @Override
    public @NotNull YamlArray toYaml() {
        YamlArray a = new YamlArray();
        this.forEach(value -> a.add(value.toYaml()));
        return a;
    }

    @Override
    public @NotNull JsonArray toJson() {
        JsonArray a = new JsonArray();
        this.forEach(value -> a.add(value.toJson()));
        return a;
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

    public List<FileElement> asList() {
        return elements;
    }

    @Override
    public String toString() {
        return "FileArray{" + Arrays.toString(elements.toArray()) + "}";
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof FileArray && ((FileArray) o).elements.equals(elements));
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}
