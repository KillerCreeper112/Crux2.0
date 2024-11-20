package killercreepr.crux.core.text.hook.prefix;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectTag;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NothingHookedPrefixBuilder implements HookedPrefixBuilder {
    @Override
    public @Nullable FormatPrefix buildPrefix(@NotNull HookedObjectTag<?, ?> hookedObjectTag, @Nullable FormatPrefix builtPrefix,
                                              @Nullable TagsPrefixBuilder builder) {
        if(builder == null) return null;
        return builtPrefix;
    }

    @Override
    public String toString() {
        return "NothingHookedPrefixBuilder{}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof NothingHookedPrefixBuilder)) return false;
        return true;
    }
}
