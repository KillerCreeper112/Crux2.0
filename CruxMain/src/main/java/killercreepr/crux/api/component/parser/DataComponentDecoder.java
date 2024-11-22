package killercreepr.crux.api.component.parser;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.core.component.parser.TextDataComponentDecoder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DataComponentDecoder {
    static DataComponentDecoder componentDecoder(){
        return new TextDataComponentDecoder();
    }

    @NotNull
    Collection<TypedDataComponent<?>> parseComponents(@NotNull String input);
}
