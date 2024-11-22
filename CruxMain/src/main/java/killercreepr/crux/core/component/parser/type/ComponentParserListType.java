package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ComponentParserListType<T> implements ComponentTextInputParser<List<T>> {
    protected final @NotNull ComponentTextInputParser<T> valueParser;
    public ComponentParserListType(@NotNull ComponentTextInputParser<T> valueParser) {
        this.valueParser = valueParser;
    }

    @Override
    public @NotNull List<T> decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Collection<?> l)) throw new IllegalArgumentException(object + " is not a collection!");
        List<T> list = new ArrayList<>();
        for(Object o : l){
            //if(o == null) continue;
            T parsed = valueParser.decodeObject(o);
            list.add(parsed);
        }
        return list;
    }

    @Override
    public @NotNull Object encodeObject(@NotNull List<T> object) {
        List<Object> list = new ArrayList<>();
        for(T o : object){
            list.add(valueParser.encodeObject(o));
        }
        return list;
    }
}
