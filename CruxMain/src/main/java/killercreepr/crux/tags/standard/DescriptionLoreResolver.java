package killercreepr.crux.tags.standard;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DescriptionLoreResolver implements StringListResolver {
    @Override
    public @NotNull String identifier() {
        return "description";
    }

    @Override
    public @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        String text = ctx.deserializeString(args.get(0));
        int maxLength;
        if(args.has(1)){
            maxLength = Integer.parseInt(ctx.deserializeString(args.get(1)));
        }else maxLength = 30;
        return CruxString.buildDescription(text, maxLength);
    }
}
