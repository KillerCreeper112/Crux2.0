package killercreepr.crux.api.component.parser;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.core.component.parser.TextComponentParser;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ComponentParser {
    static ComponentParser componentParser(){
        return new TextComponentParser();
    }

    @NotNull
    Collection<TypedDataComponent<?>> parseComponents(@NotNull String input);
}
