package killercreepr.crux.api.component.parser.persistent;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;
import killercreepr.crux.core.component.parser.persistent.MappedPersistentComponentInputParser;

import java.util.function.Function;

public interface PersistentComponentInputParser<T> extends ComponentTextInputParser<T>, PersistentDataSerializer<T> {
    static <T> MapBuilder<T> mapBuilder(){
        return new MappedPersistentComponentInputParser.Builder<>();
    }

    interface MapBuilder<T>{
        MapBuilder<T> field(String id, ComponentInputField<T> field);

        MapBuilder<T> output(Function<ComponentParseContext, T> output);

        PersistentComponentInputParser<T> apply(Function<ComponentParseContext, T> output);

        PersistentComponentInputParser<T> build();
    }
}
