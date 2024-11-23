package killercreepr.crux.api.component.parser;

public interface InputDecodeContext {
    static InputDecodeContext context(Object value){
    }

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
