package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JoinResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "join";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        var delimiter = args.get(0);
        int index = 1;
        var fullList = new ArrayList<String>();
        while(args.has(index)){
            var got = args.get(index);
            fullList.add("{" + got + "}");
            index++;
        }
        var parsed = context.deserializeStringList(fullList);
        return String.join(delimiter, parsed);
    }
}
