
package killercreepr.cruxconfig.config.common.yaml.element;

import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.crux.core.codec.node.ArrayDataNode;
import killercreepr.crux.core.codec.node.GenericDataNode;
import killercreepr.crux.core.codec.node.ObjectDataNode;
import killercreepr.cruxconfig.config.common.element.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YamlElement implements DataNode {
    public static @NotNull YamlElement fromDataNode(@Nullable DataNode e){
        if(e == null) return YamlNull.INSTANCE;
        if(e instanceof YamlElement d) return d;

        if(e instanceof GenericDataNode s) return new YamlGeneric(s.getValue());
        if(e instanceof ArrayDataNode s){
            var yaml = new YamlArray(s.size());
            s.forEachDataNode(yaml::add);
            return yaml;
        }
        if(e instanceof ObjectDataNode s){
            var yaml = new YamlObject();
            s.forEachDataPair(yaml::put);
            return yaml;
        }
        throw new UnsupportedOperationException(e.getClass().getSimpleName());
    }

    public boolean isYamlObject() {
        return this instanceof YamlObject;
    }

    public boolean isYamlPrimitive() {
        return this instanceof YamlPrimitive;
    }

    public boolean isYamlArray(){
        return this instanceof YamlArray;
    }

    public YamlObject getAsYamlObject() {
        if (isYamlObject()) {
            return (YamlObject) this;
        }
        throw new IllegalStateException("Not a Yaml Object: " + this);
    }

    public YamlArray getAsYamlArray() {
        if (isYamlArray()) {
            return (YamlArray) this;
        }
        throw new IllegalStateException("Not a Yaml Array: " + this);
    }

    public YamlPrimitive getAsYamlPrimitive() {
        if (isYamlPrimitive()) {
            return (YamlPrimitive) this;
        }
        throw new IllegalStateException("Not a Yaml Primitive: " + this);
    }

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
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public boolean isArrayData() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public boolean isString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public boolean isNumber() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public boolean isBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public DataObject asObjectData() {
        return getAsYamlObject().asObjectData();
    }

    @Override
    public DataArray asArrayData() {
        return getAsYamlArray().asArrayData();
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
