package killercreepr.crux.core.text.hook.prefix;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectTag;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
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
