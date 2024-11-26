package killercreepr.crux.core.component.parser.hybrid.text.persistence;

import com.google.common.collect.Lists;
import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.persistence.type.ListTagType;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ElementDataListType<T> implements ListPersistentDataType {
    protected final @NotNull Class<T> complexType;
    protected final @NotNull TextInputField<T, ?> element;
    protected final @NotNull TextInputResultParser<T> resultParser;
    protected final ListPersistentDataType listPersist;

    public ElementDataListType(@NotNull Class<T> complexType, @NotNull TextInputField<T, ?> element, @NotNull TextInputResultParser<T> resultParser) {
        this.complexType = complexType;
        this.element = element;
        this.resultParser = resultParser;
        this.listPersist = (ListPersistentDataType) element.inputParser().dataType();
    }

    @Override
    public @NotNull Class getPrimitiveType() {
        return (Class<List<Object>>) element.inputParser().dataType().getPrimitiveType();
    }

    @Override
    public @NotNull Class getComplexType() {
        return complexType;
    }

    @Override
    public @NotNull List<Object> toPrimitive(@NotNull Object complex, @NotNull PersistentDataAdapterContext context) {
        Object object;
        try{
            T test =(T) complex;
            object = element.parseFieldObject(complex);
        }catch (ClassCastException ignored){
            object = complex;
        }

        PersistentDataType type = element.inputParser().dataType();
        return (List<Object>) type.toPrimitive(object, context);
    }

    @Override
    public @NotNull T fromPrimitive(@NotNull Object primitive, @NotNull PersistentDataAdapterContext context) {
        PersistentDataType type = element.inputParser().dataType();
        Object parsed = type.fromPrimitive(primitive, context);
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);
    }

    @Override
    public @NotNull PersistentDataType elementType() {
        return listPersist.elementType();
    }
}
