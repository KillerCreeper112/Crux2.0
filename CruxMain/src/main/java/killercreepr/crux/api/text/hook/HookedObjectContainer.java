package killercreepr.crux.api.text.hook;

import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectContainer;
import killercreepr.crux.core.text.hook.StringListHookedObjectContainer;
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
