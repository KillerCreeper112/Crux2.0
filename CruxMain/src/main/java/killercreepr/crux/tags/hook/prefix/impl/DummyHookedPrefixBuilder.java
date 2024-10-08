package killercreepr.crux.tags.hook.prefix.impl;

import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DummyHookedPrefixBuilder implements HookedPrefixBuilder {
    @Override
    public @Nullable FormatPrefix buildPrefix(@NotNull HookedObjectTag<?, ?> hookedObjectTag, @Nullable FormatPrefix builtPrefix,
                                              @Nullable TagsPrefixBuilder builder) {
        return builtPrefix;
    }

    @Override
    public String toString() {
        return "DummyHookedPrefixBuilder{}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof DummyHookedPrefixBuilder)) return false;
        return true;
    }
}
