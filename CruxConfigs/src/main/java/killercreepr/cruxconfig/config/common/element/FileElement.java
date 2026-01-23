
package killercreepr.cruxconfig.config.common.element;

import com.google.gson.*;
import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.crux.core.codec.node.ArrayDataNode;
import killercreepr.crux.core.codec.node.GenericDataNode;
import killercreepr.crux.core.codec.node.ObjectDataNode;
import killercreepr.cruxconfig.config.common.yaml.element.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileElement implements DataNode {
    public static @NotNull FileElement fromJson(@Nullable JsonElement e){
        if(e == null || e instanceof JsonNull) return FileNull.INSTANCE;
        if(e instanceof JsonPrimitive s) return FilePrimitive.fromJson(s);
        if(e instanceof JsonArray s) return FileArray.fromJson(s);
        if(e instanceof JsonObject s) return FileObject.fromJson(s);
        throw new UnsupportedOperationException(e.getClass().getSimpleName());
    }

    public static @NotNull FileElement fromYaml(@Nullable YamlElement e){
        if(e == null || e instanceof YamlNull) return FileNull.INSTANCE;
        if(e instanceof YamlPrimitive s) return FilePrimitive.fromYaml(s);
        if(e instanceof YamlGeneric s) return FileGeneric.fromYaml(s);
        if(e instanceof YamlArray s) return FileArray.fromYaml(s);
        if(e instanceof YamlObject s) return FileObject.fromYaml(s);
        throw new UnsupportedOperationException(e.getClass().getSimpleName());
    }

    public static @NotNull FileElement fromDataNode(@Nullable DataNode e){
        if(e == null) return FileNull.INSTANCE;
        if(e instanceof FileElement d) return d;

        if(e instanceof GenericDataNode s) return FilePrimitive.fromDataNode(s);
        if(e instanceof ArrayDataNode s) return FileArray.fromDataNode(s);
        if(e instanceof ObjectDataNode s) return FileObject.fromDataNode(s);
        throw new UnsupportedOperationException(e.getClass().getSimpleName());
    }

    public boolean isFileObject() {
        return this instanceof FileObject;
    }

    public boolean isFilePrimitive() {
        return this instanceof FilePrimitive;
    }

    public boolean isFileArray(){
        return this instanceof FileArray;
    }

    public FileObject getAsFileObject() {
        if (isFileObject()) {
            return (FileObject) this;
        }
        throw new IllegalStateException("Not a File Object: " + this);
    }

    public FileArray getAsFileArray() {
        if (isFileArray()) {
            return (FileArray) this;
        }
        throw new IllegalStateException("Not a File Array: " + this);
    }

    public FilePrimitive getAsFilePrimitive() {
        if (isFilePrimitive()) {
            return (FilePrimitive) this;
        }
        throw new IllegalStateException("Not a File Primitive: " + this);
    }

    public @NotNull YamlElement toYaml(){ throw new UnsupportedOperationException(getClass().getSimpleName()); }
    public @NotNull JsonElement toJson(){ throw new UnsupportedOperationException(getClass().getSimpleName()); }

    public Object getAsObject(){
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public double getAsDouble() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public float getAsFloat() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public long getAsLong() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public int getAsInt() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public byte getAsByte() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public short getAsShort() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public boolean isObjectData() {
        return isFileObject();
    }

    @Override
    public boolean isArrayData() {
        return isFileArray();
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public DataObject asObjectData() {
        return getAsFileObject();
    }

    @Override
    public DataArray asArrayData() {
        return getAsFileArray();
    }

    @Override
    public String asString() {
        return getAsString();
    }

    @Override
    public Number asNumber() {
        return getAsNumber();
    }

    @Override
    public boolean asBoolean() {
        return getAsBoolean();
    }
}
