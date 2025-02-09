package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RomanNumeralResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "roman_numeral";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        return resolve(args.get(0));
    }

    public static @NotNull String resolve(@NotNull String text){
        return CruxMath.numeral((int) Double.parseDouble(text));
    }
}
