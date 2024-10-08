package killercreepr.crux.tags.hook.prefix.impl;

import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

public class PrefixAddHookedPrefixBuilder implements HookedPrefixBuilder {
    protected final @Nullable FormatPrefix prefix;
    public PrefixAddHookedPrefixBuilder(@Nullable FormatPrefix prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "PrefixAddHookedPrefixBuilder{prefix=" + prefix + "}" ;
    }

    @Override
    public @Nullable FormatPrefix buildPrefix(@NotNull HookedObjectTag<?, ?> hookedObjectTag, @Nullable FormatPrefix builtPrefix,
                                              @Nullable TagsPrefixBuilder prefixBuilder) {
        return FormatPrefix.add(prefix, builtPrefix);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof PrefixAddHookedPrefixBuilder d)) return false;
        return Objects.equals(d.prefix, prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(prefix);
    }
}
