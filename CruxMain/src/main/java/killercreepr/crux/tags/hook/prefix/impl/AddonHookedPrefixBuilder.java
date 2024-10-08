package killercreepr.crux.tags.hook.prefix.impl;

import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AddonHookedPrefixBuilder implements HookedPrefixBuilder {
    protected final @NotNull HookedPrefixBuilder first;
    protected final @NotNull HookedPrefixBuilder second;

    public AddonHookedPrefixBuilder(@NotNull HookedPrefixBuilder first, @NotNull HookedPrefixBuilder second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "AddonHookedPrefixBuilder{first=" + first + ", second=" + second + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof AddonHookedPrefixBuilder d)) return false;
        return d.first.equals(first) && d.second.equals(second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public @Nullable FormatPrefix buildPrefix(@NotNull HookedObjectTag<?, ?> hooked, @Nullable FormatPrefix builtPrefix, @Nullable TagsPrefixBuilder builder) {
        return FormatPrefix.add(first.buildPrefix(hooked, builtPrefix, builder), second.buildPrefix(hooked, builtPrefix, builder));
    }
}
