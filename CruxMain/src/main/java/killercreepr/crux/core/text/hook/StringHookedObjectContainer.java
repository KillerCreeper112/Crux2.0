package killercreepr.crux.core.text.hook;

import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringTagContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StringHookedObjectContainer implements HookedObjectContainer<StringHookedObjectTag<?>> {
    protected final Map<ObjectTag<?>, Collection<StringHookedObjectTag<?>>> hookedObjects = new HashMap<>();

    @Override
    public @NotNull Collection<StringHookedObjectTag<?>> getHookedObjects() {
        Collection<StringHookedObjectTag<?>> list = new HashSet<>();
        hookedObjects.values().forEach(list::addAll);
        return list;
    }

    @Override
    public StringHookedObjectContainer addAll(@Nullable HookedObjectContainer<StringHookedObjectTag<?>> hooked) {
        if(hooked==null) return this;
        return addAll(hooked.getHookedObjects());
    }

    @Override
    public StringHookedObjectContainer addAll(@Nullable Collection<StringHookedObjectTag<?>> hooked) {
        if(hooked==null) return this;
        hooked.forEach(this::add);
        return this;
    }

    @Override
    public StringHookedObjectContainer add(@Nullable StringHookedObjectTag<?> hooked) {
        if(hooked==null) return this;
        hookedObjects.computeIfAbsent(hooked.getObjectTag(), (e) -> new HashSet<>()).add(hooked);
        //hookedObjects.put(hooked.getObjectTag(), hooked);
        return this;
    }

    @Override
    public @NotNull StringTagContainer toTags(@NotNull TagParser parser) {
        StringTagContainer tags = new StringTagContainer(parser);
        getHookedObjects().forEach(hooked -> tags.addAll(hooked.getTags()));
        return tags;
    }
}
