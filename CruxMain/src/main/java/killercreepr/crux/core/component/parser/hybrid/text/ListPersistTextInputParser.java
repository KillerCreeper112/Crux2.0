package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.hybrid.PersistInputParser;
import killercreepr.crux.api.component.parser.hybrid.PersistTextInputParser;
import killercreepr.crux.core.persistence.type.ListTagType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListPersistTextInputParser<T> implements PersistTextInputParser<List<T>> {
    protected final @NotNull PersistInputParser<T> elementParser;
    protected final @NotNull PersistentDataType<?, List<T>> dataType;

    public ListPersistTextInputParser(@NotNull PersistInputParser<T> elementParser, @Nullable PersistentDataType<?, List<T>> dataType) {
        this.elementParser = elementParser;
        this.dataType = dataType == null ? buildDataType(elementParser.dataType()) : dataType;
    }

    public PersistentDataType<?, List<T>> buildDataType(@NotNull PersistentDataType<?, T> elementType){
        return new ListTagType<>(elementType);
    }

    @Override
    public @NotNull PersistentDataType<?, List<T>> dataType() {
        return dataType;
    }

    @Override
    public @NotNull List<T> decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Collection<?> list)) throw new IllegalArgumentException(object + " is not a Collection!");
        List<T> parsed = new ArrayList<>();
        for(Object o : list){
            T result = elementParser.decodeObject(o);
            parsed.add(result);
        }
        return parsed;
    }

    @Override
    public @NotNull Object encodeObject(@NotNull List<T> object) {
        List<Object> list = new ArrayList<>();
        for(T result : object){
            list.add(elementParser.encodeObject(result));
        }
        return list;
    }
}
