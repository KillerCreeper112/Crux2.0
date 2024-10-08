package killercreepr.crux.tags.context;

import killercreepr.crux.tags.context.impl.AddonFormatPrefix;
import killercreepr.crux.tags.context.impl.ConstantFormatPrefix;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

public interface FormatPrefix {
    static @NotNull FormatPrefix simple(@NotNull String constantPrefix){
        return new ConstantFormatPrefix(constantPrefix);
    }
    static @NotNull FormatPrefix empty(){
        return EMPTY;
    }
    FormatPrefix EMPTY = simple("");

    static @NotNull FormatPrefix add(@Nullable FormatPrefix first, @Nullable FormatPrefix second){
        if(first==null && second== null) return empty();
        if(first==null) return second;
        if(second==null) return first;
        return new AddonFormatPrefix(first, second);
    }

    @NotNull String buildPrefix(@NotNull TagResolver<?> resolver);
}
