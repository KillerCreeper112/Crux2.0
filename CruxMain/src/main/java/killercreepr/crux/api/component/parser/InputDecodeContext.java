package killercreepr.crux.api.component.parser;

import killercreepr.crux.core.component.parser.hybrid.SimpleInputDecodeContext;

import java.util.List;
import java.util.Map;

public interface InputDecodeContext {
    static InputDecodeContext context(Object value){
        return new SimpleInputDecodeContext(value);
    }

    List<?> toList();
    Map<?, ?> toMap();

    <T> T get();

    <T> T get(String id);
    default <T> T getOptional(String id){
        return getOptional(id, null);
    }
    default <T> T getOptional(String id, T fallback){
        T gotted = get(id);
        if(gotted == null) return fallback;
        return gotted;
    }
}
