package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.persistent.ComponentParseContext;
import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SimpleComponentParseContext<T> implements ComponentParseContext {
    protected final PersistentTextParser<T> parser;
    protected final Object values;

    public SimpleComponentParseContext(PersistentTextParser<T> parser, Object values) {
        this.parser = parser;
        this.values = values;
    }

    public Map<?, ?> toMap(){
        return (Map<?, ?>) values;
    }

    public List<?> toList(){
        return (List<?>) values;
    }

    @Override
    public <E> E decode(@NotNull Function<Object, E> function) {
        try{
            return (E) values;
        }catch (ClassCastException ignored){}
        return function.apply(values);
    }

    @Override
    public <E> E decode(@NotNull String id) {
        try{
            return (E) values;
        }catch (ClassCastException ignored){}
        Object object = toMap().get(id);
        try{
            return (E) object;
        }catch (ClassCastException ignored){}
        if(!(parser instanceof MappedPersistentComponentInputParser<T> m)) throw new UnsupportedOperationException(
            "Parser must be a MappedPersistentComponentInputParser!"
        );
        return (E) m.getInputParsers().get(id).textInputParser().decodeObject(object);
    }

    @Override
    public <E> @Nullable E decodeOptional(@NotNull String id) {
        if(!toMap().containsKey(id)) return null;
        return decode(id);
    }
}
