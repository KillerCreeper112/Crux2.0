package killercreepr.crux.tags.hook.prefix.impl;

import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SuffixAddHookedPrefixBuilder implements HookedPrefixBuilder {
    protected final @Nullable FormatPrefix suffix;
    public SuffixAddHookedPrefixBuilder(@Nullable FormatPrefix suffix) {
        this.suffix = suffix;
    }

    @Override
    public @Nullable FormatPrefix buildPrefix(@NotNull HookedObjectTag<?, ?> hookedObjectTag, @Nullable FormatPrefix builtPrefix,
                                              @Nullable TagsPrefixBuilder prefixBuilder) {
        return FormatPrefix.add(builtPrefix, suffix);
    }

    @Override
    public String toString() {
        return "SuffixAddHookedPrefixBuilder{suffix=" + suffix + "}" ;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SuffixAddHookedPrefixBuilder d)) return false;
        return Objects.equals(d.suffix, suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(suffix);
    }
}
