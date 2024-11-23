package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import org.jetbrains.annotations.NotNull;

public interface TextInputResultParser<T> {
    T parse(@NotNull InputDecodeContext ctx);
}
