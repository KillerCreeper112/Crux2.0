package killercreepr.crux.context;

import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.provider.StringTagProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//todo change this to simple or something
public class EquationContext implements InputContext {
    protected final @NotNull Format format;
    protected final @Nullable StringTagProvider tags;

    public EquationContext(@NotNull Format format, @Nullable StringTagProvider tags) {
        this.format = format;
        this.tags = tags;
    }

    public @NotNull Format getFormat() {
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
