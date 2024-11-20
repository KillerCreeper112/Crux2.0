package killercreepr.crux.core.text.hook;

import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringListTagContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StringListHookedObjectContainer implements HookedObjectContainer<StringListHookedObjectTag<?>> {
    protected final Map<ObjectTag<?>, Collection<StringListHookedObjectTag<?>>> hookedObjects = new HashMap<>();

    @Override
    public @NotNull Collection<StringListHookedObjectTag<?>> getHookedObjects() {
        Collection<StringListHookedObjectTag<?>> list = new HashSet<>();
        hookedObjects.values().forEach(list::addAll);
        return list;
    }

    @Override
    public StringListHookedObjectContainer addAll(@Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hooked) {
        if(hooked==null) return this;
        return addAll(hooked.getHookedObjects());
    }

    @Override
    public StringListHookedObjectContainer addAll(@Nullable Collection<StringListHookedObjectTag<?>> hooked) {
        if(hooked==null) return this;
        hooked.forEach(this::add);
        return this;
    }

    @Override
    public StringListHookedObjectContainer add(@Nullable StringListHookedObjectTag<?> hooked) {
        if(hooked==null) return this;
        hookedObjects.computeIfAbsent(hooked.getObjectTag(), (e) -> new HashSet<>()).add(hooked);
        //hookedObjects.put(hooked.getObjectTag(), hooked);
        return this;
    }

    @Override
    public @NotNull StringListTagContainer toTags(@NotNull TagParser parser) {
        StringListTagContainer tags = new StringListTagContainer(parser);
        getHookedObjects().forEach(hooked -> tags.addAll(hooked.getTags()));
        return tags;
    }
}
