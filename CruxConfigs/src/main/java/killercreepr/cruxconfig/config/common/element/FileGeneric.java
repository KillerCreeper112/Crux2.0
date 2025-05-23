package killercreepr.cruxconfig.config.common.element;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import org.jetbrains.annotations.NotNull;

public class FileGeneric extends FileElement {
    public static @NotNull FileGeneric fromYaml(@NotNull YamlGeneric e){
        return new FileGeneric(e.getAsObject());
    }

    protected final Object value;
    public FileGeneric(Object value) {
        this.value = value;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    public @NotNull YamlElement toYaml() {
        return new YamlGeneric(value);
    }

    @Override
    public @NotNull JsonElement toJson() {
        if(value instanceof String s) return new JsonPrimitive(s);
        if(value instanceof Boolean s) return new JsonPrimitive(s);
        if(value instanceof Number s) return new JsonPrimitive(s);
        if(value instanceof Character s) return new JsonPrimitive(s);
        return super.toJson();
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
        if(value instanceof Number n) return n;
        //if (value instanceof Number n) return CruxObjects.parseNumber(n);
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
        return "FileGeneric{" + value.toString() + "}";
    }
}
