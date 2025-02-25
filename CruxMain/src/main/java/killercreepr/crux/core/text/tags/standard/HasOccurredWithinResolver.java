package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HasOccurredWithinResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "has_occurred_within";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        double value = CruxMath.evaluate(ctx.deserializeString(args.get(0)));
        int ticks = (int) CruxMath.evaluate(ctx.deserializeString(args.get(1)));
        return CruxMath.hasOccurredWithin((long) value, ticks) + "";
    }
}
