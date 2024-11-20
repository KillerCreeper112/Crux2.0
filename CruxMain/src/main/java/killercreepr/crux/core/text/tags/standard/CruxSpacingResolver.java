package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxSpacingResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "crux_space";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        int value = args.parseInt(0);
        return "<font:\"crux:spacing\">" + CruxString.unicodeSpacing(value) + "</font>";
    }
}
