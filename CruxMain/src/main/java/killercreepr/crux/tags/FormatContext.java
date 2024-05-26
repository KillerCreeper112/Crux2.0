package killercreepr.crux.tags;

import killercreepr.crux.tags.format.Format;
import org.jetbrains.annotations.NotNull;

public class FormatContext {
    protected final @NotNull Format format;

    public FormatContext(@NotNull Format format) {
        this.format = format;
    }

    public @NotNull Format getFormat() {
        return format;
    }
}
