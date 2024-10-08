package killercreepr.crux.tags.context.impl;

import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ConstantFormatPrefix implements FormatPrefix {
    protected final @NotNull String prefix;
    public ConstantFormatPrefix(@NotNull String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "ConstantFormatPrefix{prefix=" + prefix + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof ConstantFormatPrefix p)) return false;
        return prefix.equals(p.getPrefix());
    }

    public @NotNull String getPrefix() {
        return prefix;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(prefix);
    }

    @Override
    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
        return prefix;
    }
}
