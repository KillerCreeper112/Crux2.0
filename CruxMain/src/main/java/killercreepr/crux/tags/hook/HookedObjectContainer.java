package killercreepr.crux.tags.hook;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.hook.impl.StringHookedObjectContainer;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface HookedObjectContainer<T extends HookedObjectTag<?, ?>> {
    static @NotNull StringHookedObjectContainer string(){
        return new StringHookedObjectContainer();
    }
    static @NotNull StringListHookedObjectContainer stringList(){
        return new StringListHookedObjectContainer();
    }

    @NotNull Collection<T> getHookedObjects();
    HookedObjectContainer<T> addAll(@Nullable HookedObjectContainer<T> hooked);
    HookedObjectContainer<T> addAll(@Nullable Collection<T> hooked);
    HookedObjectContainer<T> add(@Nullable T hooked);
    @NotNull TagContainer<?> toTags(@NotNull TagParser parser);
}
