package killercreepr.crux.api.text.format;

import killercreepr.crux.api.text.resolver.TagResolver;
import killercreepr.crux.core.text.format.prefix.AddonFormatPrefix;
import killercreepr.crux.core.text.format.prefix.ConstantFormatPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
