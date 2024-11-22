package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.persistent.ComponentParseContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SimpleComponentParseContext<T> implements ComponentParseContext {
    protected final MappedPersistentComponentInputParser<T> parser;
    protected final Map<?, ?> values;

    public SimpleComponentParseContext(MappedPersistentComponentInputParser<T> parser, Map<?, ?> values) {
        this.parser = parser;
        this.values = values;
    }

    @Override
    public <E> E decode(@NotNull String id) {
        return (E) parser.getInputParsers().get(id).textInputParser().decodeObject(values.get(id));
    }

    @Override
    public <E> @Nullable E decodeOptional(@NotNull String id) {
        if(!values.containsKey(id)) return null;
        return decode(id);
    }
}
