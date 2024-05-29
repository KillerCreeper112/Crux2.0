
package killercreepr.cruxconfig.config.common.yaml.element;

import killercreepr.cruxconfig.config.common.element.FileElement;

public abstract class YamlElement extends FileElement {
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

/*    public Object getAsObject(){
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
    }*/
}
