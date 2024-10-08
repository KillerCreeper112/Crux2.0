package killercreepr.crux.tags;

import killercreepr.crux.tags.container.*;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectContainer;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.hook.impl.StringHookedObjectContainer;
import killercreepr.crux.tags.hook.impl.StringHookedObjectTag;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectContainer;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class CruxTags implements TagParser {
    protected final Collection<ObjectTag<?>> tags = new HashSet<>();

    @Override
    public CruxTags register(@NotNull ObjectTag<?> tag) {
        tags.add(tag);
        return this;
    }

    @Override
    public <T> @NotNull Collection<ObjectTag<T>> locateTags(@NotNull T object){
        Collection<ObjectTag<T>> list = new HashSet<>();
        tags.forEach(tag ->{
            if(!tag.canResolve(object)) return;
            list.add((ObjectTag<T>) tag);
        });
        return list;
    }
    @Override
    public <T> @Nullable StringTagContainer buildStringTags(@NotNull T object){
        return buildStringTags(object, null);
    }
    @Override
    public <T> @Nullable StringTagContainer buildStringTags(@NotNull T object, @Nullable TagsPrefixBuilder prefixBuilder){
        StringTagContainer tag = new StringTagContainer(this);
        locateTags(object).forEach(objectTag ->{
            TagContainer<StringResolver> tags = objectTag.requestStrings(object, this);
            FormatPrefix prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);

            HookedObjectContainer<StringHookedObjectTag<?>> hookedTags = objectTag.hookStrings(object, this);
            if(hookedTags != null){
                hookedTags.getHookedObjects().forEach(hooked ->{
                    FormatPrefix pre = prefixBuilder == null ? hooked.getObjectTag().defaultPrefix() : prefixBuilder.buildHookedPrefix(
                        objectTag, object, hooked
                    );
                    tag.addAll(hooked.getTags(), hooked.getPrefix().buildPrefix(hooked, pre, prefixBuilder));
                });
            }
        });
        return tag;
    }

    @Override
    public <T> @NotNull StringHookedObjectContainer hookStrings(@NotNull T object, @NotNull HookedPrefixBuilder prefix){
        StringHookedObjectContainer container = HookedObjectContainer.string();
        locateTags(object).forEach(objectTag ->{
            TagContainer<StringResolver> tags = objectTag.requestStrings(object, this);
            if(tags != null){
                container.add(new StringHookedObjectTag<>(object, objectTag, tags, prefix));
            }

            HookedObjectContainer<StringHookedObjectTag<?>> hookedTags = objectTag.hookStrings(object, this);
            if(hookedTags != null){
                hookedTags.addAll(container);
                hookedTags.getHookedObjects().forEach(hooked ->{
                    container.add(hooked.withAddedPrefix(prefix));
                });

                //container.addAll(hookedTags);
            }
        });
        return container;
    }

    @Override
    public <T> @Nullable StringListTagContainer buildStringListTags(@NotNull T object){
        return buildStringListTags(object, null);
    }
    @Override
    public <T> @Nullable StringListTagContainer buildStringListTags(@NotNull T object, @Nullable TagsPrefixBuilder prefixBuilder){
        StringListTagContainer tag = new StringListTagContainer(this);
        locateTags(object).forEach(objectTag ->{
            TagContainer<StringListResolver> tags = objectTag.requestStringLists(object, this);
            FormatPrefix prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);

            HookedObjectContainer<StringListHookedObjectTag<?>> hookedTags = objectTag.hookStringLists(object, this);
            if(hookedTags != null){
                hookedTags.getHookedObjects().forEach(hooked ->{
                    FormatPrefix pre = prefixBuilder == null ? hooked.getObjectTag().defaultPrefix() : prefixBuilder.buildHookedPrefix(
                        objectTag, object, hooked
                    );
                    tag.addAll(hooked.getTags(), hooked.getPrefix().buildPrefix(hooked, pre, prefixBuilder));
                });
            }
        });
        return tag;
    }

    @Override
    public <T> @NotNull StringListHookedObjectContainer hookStringLists(@NotNull T object, @NotNull HookedPrefixBuilder prefix){
        StringListHookedObjectContainer container = HookedObjectContainer.stringList();
        locateTags(object).forEach(objectTag ->{
            TagContainer<StringListResolver> tags = objectTag.requestStringLists(object, this);
            if(tags != null){
                container.add(new StringListHookedObjectTag<>(object, objectTag, tags, prefix));
            }

            HookedObjectContainer<StringListHookedObjectTag<?>> hookedTags = objectTag.hookStringLists(object, this);
            if(hookedTags != null){
                hookedTags.addAll(container);
                hookedTags.getHookedObjects().forEach(hooked ->{
                    container.add(hooked.withAddedPrefix(prefix));
                });
                //hookedTags.getHookedObjects().forEach(container::add);
            }
        });
        return container;
    }

    @Override
    public <T> @Nullable MergedTagContainer buildTags(@NotNull T object){
        SimpleMergedTagContainer tag = new SimpleMergedTagContainer(this);
        locateTags(object).forEach(objectTag ->{
            tag.getStringTags().addAll(buildStringTags(object));
            tag.getStringListTags().addAll(buildStringListTags(object));
        });
        return tag;
    }

    public static class Builder implements TagParser.Builder{
        protected final Collection<ObjectTag<?>> tags = new HashSet<>();
        public Builder addTag(@NotNull ObjectTag<?> tag) {
            tags.add(tag);
            return this;
        }

        public Builder addTags(@NotNull Collection<ObjectTag<?>> tags){
            this.tags.addAll(tags);
            return this;
        }

        public @NotNull CruxTags build(){
            CruxTags tags = new CruxTags();
            this.tags.forEach(tags::register);
            return tags;
        }
    }
}
