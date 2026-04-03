package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NullElseResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "null_else";
    }

    //<null_else:<text>:<fallback>>
    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        var check = context.deserializeString(args.get(0));
        if(check.equalsIgnoreCase("null")) return context.deserializeString(args.get(1));
        return check;
    }
}
