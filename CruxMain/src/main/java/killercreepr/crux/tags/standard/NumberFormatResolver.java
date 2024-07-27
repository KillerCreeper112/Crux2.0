package killercreepr.crux.tags.standard;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxNumberFormat;
import killercreepr.crux.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumberFormatResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "f";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        double value = args.parseDouble(0);
        int decimalPlaces = args.parseIntOr(1, 0);
        return resolve(value, decimalPlaces);
    }

    public static @NotNull String resolve(double value, int decimalPlaces){
        return CruxMath.buildOrGetDecimalFormat(decimalPlaces).format(value);
    }
}
