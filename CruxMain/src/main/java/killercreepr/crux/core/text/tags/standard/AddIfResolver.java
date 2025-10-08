package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddIfResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "add_if";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        String ifCheck = ctx.deserializeString(args.get(0));
        if(!CruxString.parseBoolean(CruxMath.evaluateEvalEx(ifCheck))) return "";
        int index = 1;
        StringBuilder builder = new StringBuilder();
        while(args.has(index)){
            String text = ctx.deserializeString(args.get(index));
            builder.append(text);
            index++;
        }
        return builder.toString();
    }
}
