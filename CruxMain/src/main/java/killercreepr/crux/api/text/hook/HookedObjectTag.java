package killercreepr.crux.api.text.hook;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.resolver.TagResolver;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;

public interface HookedObjectTag<T, E extends TagResolver<?>> {
    @NotNull ObjectTag<T> getObjectTag();
    @NotNull T getObject();
    @NotNull
    TagContainer<E> getTags();
    @NotNull FormatPrefix getPrefix();
}
