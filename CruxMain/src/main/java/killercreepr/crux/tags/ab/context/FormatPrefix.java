package killercreepr.crux.tags.ab.context;

import killercreepr.crux.tags.ab.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public interface FormatPrefix {
    @NotNull String buildPrefix(@NotNull TagResolver<?> resolver);
}
