package killercreepr.crux.api.component.parser.persistent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ComponentParseContext {
    <E> E decode(@NotNull Function<Object, E> function);
    <E> E decode(@NotNull String id);
    <E> @Nullable E decodeOptional(@NotNull String id);
    default <E> E decodeOptional(@NotNull String id, @Nullable E fallBack){
        E object = decodeOptional(id);
        if(object == null) return fallBack;
        return object;
    }

    Map<?, ?> toMap();
    List<?> toList();
}
