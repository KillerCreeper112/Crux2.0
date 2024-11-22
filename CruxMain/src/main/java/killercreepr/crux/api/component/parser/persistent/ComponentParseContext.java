package killercreepr.crux.api.component.parser.persistent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentParseContext {
    <E> E decode(@NotNull String id);
    <E> @Nullable E decodeOptional(@NotNull String id);
    default <E> @Nullable E decodeOptional(@NotNull String id, @Nullable E fallBack){
        E object = decodeOptional(id);
        if(object == null) return fallBack;
        return object;
    }
}
