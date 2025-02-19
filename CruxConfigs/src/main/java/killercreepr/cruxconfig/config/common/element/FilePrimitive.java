package killercreepr.cruxconfig.config.common.element;

import com.google.gson.JsonPrimitive;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import org.jetbrains.annotations.NotNull;

public class FilePrimitive extends FileGeneric {
    public static @NotNull FilePrimitive fromJson(@NotNull JsonPrimitive e){
        if(e.isString()) return new FilePrimitive(e.getAsString());
        if(e.isBoolean()) return new FilePrimitive(e.getAsBoolean());
        if(e.isNumber()) return new FilePrimitive(e.getAsNumber());
        throw new UnsupportedOperationException(e.getClass().getSimpleName());
    }

    public static @NotNull FilePrimitive fromYaml(@NotNull YamlPrimitive e){
        if(e.isString()) return new FilePrimitive(e.getAsString());
        if(e.isBoolean()) return new FilePrimitive(e.getAsBoolean());
        if(e.isNumber()) return new FilePrimitive(e.getAsNumber());
        throw new UnsupportedOperationException(e.getClass().getSimpleName());
    }

    public FilePrimitive(String value){
        super(value);
    }
    public FilePrimitive(Boolean value){
        super(value);
    }
    public FilePrimitive(Number value){
        super(value);
    }

    @Override
    public @NotNull YamlPrimitive toYaml() {
        if(isString()) return new YamlPrimitive((String) value);
        if(isBoolean()) return new YamlPrimitive((Boolean) value);
        if(isNumber()) return new YamlPrimitive((Number) value);
        throw new UnsupportedOperationException(getClass().getSimpleName() + ", " + value);
    }

    @Override
    public @NotNull JsonPrimitive toJson() {
        if(isString()) return new JsonPrimitive((String) value);
        if(isBoolean()) return new JsonPrimitive((Boolean) value);
        if(isNumber()) return new JsonPrimitive((Number) value);
        throw new UnsupportedOperationException(getClass().getSimpleName() + ", " + value);
    }

    @Override
    public String toString() {
        return "FilePrimitive{" + value.toString() + "}";
    }
}
