package killercreepr.crux.core.text.context;

import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.provider.StringTagProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleInputContext implements InputContext {
    protected final @NotNull FormatSerializer format;
    protected final @Nullable StringTagProvider tags;
    public SimpleInputContext(@NotNull FormatSerializer format, @Nullable StringTagProvider tags) {
        this.format = format;
        this.tags = tags;
    }

    public @NotNull FormatSerializer getFormat() {
        return format;
    }

    public @Nullable StringTagProvider getTags() {
        return tags;
    }

    @Override
    public @NotNull String input(@NotNull String text) {
        return format.deserializeString(text, tags);
    }
}
