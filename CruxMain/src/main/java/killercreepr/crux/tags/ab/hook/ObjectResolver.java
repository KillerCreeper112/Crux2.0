package killercreepr.crux.tags.ab.hook;

import killercreepr.crux.tags.ab.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a hooked object tag.
 * I is what object should be inputted.
 * O is what object should be outputted.
 */
public interface ObjectResolver<I, O> extends TagResolver<O>, HookTag<I, O> {
    @NotNull String getPrefix();
}
