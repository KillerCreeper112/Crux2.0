package killercreepr.crux.tags.ab.tags;

import killercreepr.crux.tags.ab.hook.ObjectTag;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class Tags {
    protected final Collection<ObjectTag<?>> tags = new HashSet<>();
    public <T> @NotNull Collection<ObjectTag<T>> getTagsFromObject(@NotNull T object){
        Collection<ObjectTag<T>> list = new HashSet<>();
        tags.forEach(tag ->{
            if(!tag.canResolve(object)) return;
            list.add((ObjectTag<T>) tag);
        });
        return list;
    }

    public
}
