package killercreepr.crux.api.component.parser;

import killercreepr.crux.api.component.TypedDataComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DataComponentEncoder {
    @NotNull
    Collection<TypedDataComponent<?>> parseComponents(@NotNull String input);
}
