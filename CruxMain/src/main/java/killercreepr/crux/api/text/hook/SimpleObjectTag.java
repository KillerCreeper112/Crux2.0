package killercreepr.crux.api.text.hook;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface SimpleObjectTag<T> extends ObjectTag<T>{
    default @Nullable Map<Object, FormatPrefix> hookObjects(T object){
        return null;
    }
    @Override
    default @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull T object, Object base, Object parent, @NotNull TagParser tags) {
        Map<Object, FormatPrefix> map = hookObjects(object);
        if(map == null || map.isEmpty()) return null;
        var hooks = HookedObjectContainer.stringList();
        map.forEach((key, prefix) ->{
            hooks.addAll(tags.hookStringLists(key, base, object, prefix));
        });
        return hooks;
    }

    @Override
    default @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull T object, Object base, Object parent, @NotNull TagParser tags) {
        Map<Object, FormatPrefix> map = hookObjects(object);
        if(map == null || map.isEmpty()) return null;
        var hooks = HookedObjectContainer.string();
        map.forEach((key, prefix) ->{
            hooks.addAll(tags.hookStrings(key, base, object, prefix));
        });
        return hooks;
    }
}
