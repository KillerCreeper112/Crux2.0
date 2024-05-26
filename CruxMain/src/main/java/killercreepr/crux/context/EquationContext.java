package killercreepr.crux.context;

import killercreepr.crux.tags.container.ObjectStringHookContainer;
import killercreepr.crux.tags.format.Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EquationContext implements InputContext {
    protected final @NotNull Format format;
    protected final @Nullable ObjectStringHookContainer tags;

    public EquationContext(@NotNull Format format, @Nullable ObjectStringHookContainer tags) {
        this.format = format;
        this.tags = tags;
    }

    public @NotNull Format getFormat() {
        return format;
    }

    public @Nullable ObjectStringHookContainer getTags() {
        return tags;
    }

    @Override
    public @NotNull String input(@NotNull String text) {
        return format.setPlaceholders(text, tags);
    }
}
