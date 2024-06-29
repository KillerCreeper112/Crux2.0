package killercreepr.crux.tags.hook;

import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public interface HookedObjectTag<T, E extends TagResolver<?>> {
    @NotNull ObjectTag<T> getObjectTag();
    @NotNull T getObject();
    @NotNull
    TagContainer<E> getTags();
}
