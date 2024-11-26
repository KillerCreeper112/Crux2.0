package killercreepr.crux.core.component.parser.hybrid.text.persistence;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ElementDataType<T> implements PersistentDataType<Object, T> {
    protected final @NotNull Class<T> complexType;
    protected final @NotNull TextInputField<T, ?> element;
    protected final @NotNull TextInputResultParser<T> resultParser;

    public ElementDataType(@NotNull Class<T> complexType, @NotNull TextInputField<T, ?> element, @NotNull TextInputResultParser<T> resultParser) {
        this.complexType = complexType;
        this.element = element;
        this.resultParser = resultParser;
    }

    @Override
    public @NotNull Class<Object> getPrimitiveType() {
        return (Class<Object>) element.inputParser().dataType().getPrimitiveType();
    }

    @Override
    public @NotNull Class<T> getComplexType() {
        return complexType;
    }

    @Override
    public @NotNull Object toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
        Object object;
        try{
            T test =(T) complex;
            object = element.parseField(complex);
        }catch (ClassCastException ignored){
            object = complex;
        }

        PersistentDataType type = element.inputParser().dataType();
        return type.toPrimitive(object, context);
    }

    @Override
    public @NotNull T fromPrimitive(@NotNull Object primitive, @NotNull PersistentDataAdapterContext context) {
        PersistentDataType type = element.inputParser().dataType();
        Object parsed = type.fromPrimitive(primitive, context);
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);
    }

}
