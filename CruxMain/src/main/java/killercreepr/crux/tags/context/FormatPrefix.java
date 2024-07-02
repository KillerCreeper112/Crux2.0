package killercreepr.crux.tags.context;

import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FormatPrefix {
    static @NotNull FormatPrefix simple(@NotNull String constantPrefix){
        return new FormatPrefix() {
            @Override
            public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                return constantPrefix;
            }
        };
    }
    static @NotNull FormatPrefix empty(){
        return simple("");
    }

    static @NotNull FormatPrefix add(@Nullable FormatPrefix first, @Nullable FormatPrefix second){
        if(first==null && second== null) return empty();
        if(first==null) return second;
        if(second==null) return first;
        return new FormatPrefix() {
            @Override
            public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                return first.buildPrefix(resolver) + second.buildPrefix(resolver);
            }
        };
    }

    @NotNull String buildPrefix(@NotNull TagResolver<?> resolver);
}
