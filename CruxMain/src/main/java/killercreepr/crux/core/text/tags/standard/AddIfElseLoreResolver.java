package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddIfElseLoreResolver implements StringListResolver {
    @Override
    public @NotNull String identifier() {
        return "add_if_else";
    }

    @Override
    public @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        String ifCheck = ctx.deserializeString(args.get(0));
        if(!CruxString.parseBoolean(CruxMath.evaluateEvalEx(ifCheck))) return List.of(
            ctx.deserializeString(args.get(2))
        );
        return List.of(ctx.deserializeString(args.get(1)));
    }
}
