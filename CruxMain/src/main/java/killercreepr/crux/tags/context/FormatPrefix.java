package killercreepr.crux.tags.context;

import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public interface FormatPrefix {
    static @NotNull FormatPrefix simple(@NotNull String constantPrefix){
        return new FormatPrefix() {
            @Override
            public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                return constantPrefix;
            }
        };
    }
    @NotNull String buildPrefix(@NotNull TagResolver<?> resolver);
}
