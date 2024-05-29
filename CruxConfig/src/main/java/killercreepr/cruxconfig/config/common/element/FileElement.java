
package killercreepr.cruxconfig.config.common.element;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import killercreepr.cruxconfig.config.common.yaml.element.*;
import org.jetbrains.annotations.NotNull;

public abstract class FileElement {
    public static @NotNull FileElement fromJson(@NotNull JsonElement e){
        if(e instanceof JsonPrimitive s) return FilePrimitive.fromJson(s);
        if(e instanceof JsonArray s) return FileArray.fromJson(s);
        if(e instanceof JsonObject s) return FileObject.fromJson(s);
        throw new UnsupportedOperationException(e.getClass().getSimpleName());
    }

    public static @NotNull FileElement fromYaml(@NotNull YamlElement e){
        if(e instanceof YamlPrimitive s) return FilePrimitive.fromYaml(s);
        if(e instanceof YamlGeneric s) return FileGeneric.fromYaml(s);
        if(e instanceof YamlArray s) return FileArray.fromYaml(s);
        if(e instanceof YamlObject s) return FileObject.fromYaml(s);
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
        throw new IllegalStateException("Not a Yaml Object: " + this);
    }

    public FileArray getAsFileArray() {
        if (isFileArray()) {
            return (FileArray) this;
        }
        throw new IllegalStateException("Not a Yaml Array: " + this);
    }

    public FilePrimitive getAsFilePrimitive() {
        if (isFilePrimitive()) {
            return (FilePrimitive) this;
        }
        throw new IllegalStateException("Not a Yaml Primitive: " + this);
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
}
