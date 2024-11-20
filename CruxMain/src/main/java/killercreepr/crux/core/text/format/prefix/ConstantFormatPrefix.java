package killercreepr.crux.core.text.format.prefix;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.resolver.TagResolver;
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
