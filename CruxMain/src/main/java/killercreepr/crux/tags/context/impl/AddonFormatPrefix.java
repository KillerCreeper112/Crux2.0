package killercreepr.crux.tags.context.impl;

import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddonFormatPrefix implements FormatPrefix {
    protected final @NotNull FormatPrefix first;
    protected final @NotNull FormatPrefix second;

    public AddonFormatPrefix(@NotNull FormatPrefix first, @NotNull FormatPrefix second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "AddonFormatPrefix{first=" + first + ", second=" + second + "}";
    }

    @Override
    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
        return first.buildPrefix(resolver) + second.buildPrefix(resolver);
    }

    public @NotNull FormatPrefix getFirst() {
        return first;
    }

    public @NotNull FormatPrefix getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof AddonFormatPrefix d)) return false;
        return first.equals(d.getFirst()) && second.equals(d.getSecond());
    }
}
