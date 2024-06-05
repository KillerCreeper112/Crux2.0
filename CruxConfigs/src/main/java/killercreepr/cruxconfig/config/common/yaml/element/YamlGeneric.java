package killercreepr.cruxconfig.config.common.yaml.element;

public class YamlGeneric extends YamlElement {
    protected final Object value;
    public YamlGeneric(Object value) {
        this.value = value;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    public Object getAsObject() {
        return value;
    }

    @Override
    public boolean getAsBoolean() {
        if (isBoolean()) return (Boolean) value;
        return Boolean.parseBoolean(getAsString());
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    @Override
    public Number getAsNumber() {
        if (value instanceof Number n) return n;
        if(value instanceof String d) return Double.parseDouble(d);
        throw new UnsupportedOperationException("Primitive is neither a number nor a string");
    }

    public boolean isString() {
        return value instanceof String;
    }

    @Override
    public String getAsString() {
        return value.toString();
    }

    @Override
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    @Override
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    @Override
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }
    @Override
    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    @Override
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override
    public String toString() {
        return "YamlGeneric{" + value.toString() + "}";
    }
}
