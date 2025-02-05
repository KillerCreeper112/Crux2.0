package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.core.persistence.type.ListTagType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionPersistTextParser<T> implements PersistTextParser<Collection<T>> {
    protected final @NotNull PersistTextParser<T> elementParser;
    protected final @NotNull PersistentDataType<?, Collection<T>> dataType;

    public CollectionPersistTextParser(@NotNull PersistTextParser<T> elementParser, @Nullable PersistentDataType<?, Collection<T>> dataType) {
        this.elementParser = elementParser;
        this.dataType = dataType == null ? buildDataType(elementParser.dataType()) : dataType;
    }

    public PersistentDataType<?, Collection<T>> buildDataType(@NotNull PersistentDataType<?, T> elementType){
        return (PersistentDataType<?, Collection<T>>) (PersistentDataType) new ListTagType<>(elementType);
    }

    @Override
    public @NotNull PersistentDataType<?, Collection<T>> dataType() {
        return dataType;
    }

    @Override
    public @NotNull Collection<T> decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Collection<?> list)) throw new IllegalArgumentException(object + " is not a Collection!");
        Collection<T> parsed = new ArrayList<>();
        for(Object o : list){
            T result = elementParser.decodeObject(o);
            parsed.add(result);
        }
        return parsed;
    }

    @Override
    public @NotNull Object encodeObject(@NotNull Collection<T> object) {
        Collection<Object> list = new ArrayList<>();
        for(T result : object){
            list.add(elementParser.encodeObject(result));
        }
        return list;
    }
}
