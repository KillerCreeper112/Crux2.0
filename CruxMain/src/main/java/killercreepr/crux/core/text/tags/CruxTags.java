package killercreepr.crux.core.text.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.container.SimpleMergedTagContainer;
import killercreepr.crux.core.text.container.StringListTagContainer;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectContainer;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
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
            FormatPrefix normalPrefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, normalPrefix);

            HookedObjectContainer<StringHookedObjectTag<?>> hookedTags = objectTag.hookStrings(
                object, object, object, this
            );
            if(hookedTags != null){
                hookedTags.getHookedObjects().forEach(hooked ->{
                    tag.addAll(buildStrings(
                        object, normalPrefix,
                        hooked.getPrefix(),
                        hooked.getObject()
                    ));
                });
            }
        });
        return tag;
    }

    public <T> @NotNull StringTagContainer buildStrings(@NotNull Object baseObject,
                                                        @Nullable FormatPrefix basePrefix,
                                                        @NotNull FormatPrefix hookedObjectPrefix,
                                                        @NotNull T hookedObject){

        StringTagContainer tag = new StringTagContainer(this);
        locateTags(hookedObject).forEach(objectTag ->{
            TagContainer<StringResolver> tags = objectTag.requestStrings(hookedObject, this);
            FormatPrefix normalPrefix = FormatPrefix.add(basePrefix, hookedObjectPrefix);
            tag.addAll(tags, normalPrefix);

            var hookedTags = objectTag.hookStrings(
                hookedObject, baseObject, this, this
            );
            if(hookedTags != null){
                hookedTags.getHookedObjects().forEach(hooked ->{
                    FormatPrefix hookedPrefix = FormatPrefix.add(normalPrefix, hooked.getPrefix());
                    tag.addAll(hooked.getTags(), hookedPrefix).addAll(
                        buildStrings(baseObject, normalPrefix, hooked.getPrefix(), hooked.getObject())
                    );
                });
            }
        });
        return tag;
    }

    @Override
    public <T> @NotNull StringHookedObjectContainer hookStrings(@NotNull T object, @Nullable FormatPrefix prefix){
        return hookStrings(object, object, object, prefix);
    }

    @Override
    public @NotNull <T> StringHookedObjectContainer hookStrings(@NotNull T object, Object base, Object parent, @Nullable FormatPrefix inputPrefix) {
        StringHookedObjectContainer container = HookedObjectContainer.string();
        locateTags(object).forEach(objectTag ->{
            FormatPrefix prefix = inputPrefix == null ? objectTag.defaultPrefix() : inputPrefix;
            TagContainer<StringResolver> tags = objectTag.requestStrings(object, this);
            if(tags != null){
                container.add(new StringHookedObjectTag<>(base, parent, object, objectTag, tags, prefix));
            }

            HookedObjectContainer<StringHookedObjectTag<?>> hookedTags = objectTag.hookStrings(
                object, base, object, this
            );
            if(hookedTags != null){
                hookedTags.addAll(container);
            }
        });
        return container;
    }

    @Override
    public @NotNull <T> StringListHookedObjectContainer hookStringLists(@NotNull T object, @Nullable FormatPrefix prefix) {
        return hookStringLists(object, object, object, prefix);
    }

    @Override
    public @NotNull <T> StringListHookedObjectContainer hookStringLists(@NotNull T object, Object base, Object parent, @Nullable FormatPrefix inputPrefix) {
        StringListHookedObjectContainer container = HookedObjectContainer.stringList();
        locateTags(object).forEach(objectTag ->{
            FormatPrefix prefix = inputPrefix == null ? objectTag.defaultPrefix() : inputPrefix;
            TagContainer<StringListResolver> tags = objectTag.requestStringLists(object, this);
            if(tags != null){
                container.add(new StringListHookedObjectTag<T>(base, parent, object, objectTag, tags, prefix));
            }

            var hookedTags = objectTag.hookStringLists(
                object, base, object, this
            );
            if(hookedTags != null){
                hookedTags.addAll(container);
            }
        });
        return container;
    }

    public <T> @NotNull StringListTagContainer buildStringLists(@NotNull Object baseObject,
                                                        @Nullable FormatPrefix basePrefix,
                                                        @NotNull FormatPrefix hookedObjectPrefix,
                                                        @NotNull T hookedObject){

        StringListTagContainer tag = new StringListTagContainer(this);
        locateTags(hookedObject).forEach(objectTag ->{
            TagContainer<StringListResolver> tags = objectTag.requestStringLists(hookedObject, this);
            FormatPrefix normalPrefix = FormatPrefix.add(basePrefix, hookedObjectPrefix);
            tag.addAll(tags, normalPrefix);

            var hookedTags = objectTag.hookStringLists(
                hookedObject, baseObject, this, this
            );
            if(hookedTags != null){
                hookedTags.getHookedObjects().forEach(hooked ->{
                    FormatPrefix hookedPrefix = FormatPrefix.add(normalPrefix, hooked.getPrefix());
                    tag.addAll(hooked.getTags(), hookedPrefix).addAll(
                        buildStringLists(baseObject, normalPrefix, hooked.getPrefix(), hooked.getObject())
                    );
                });
            }
        });
        return tag;
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
            FormatPrefix normalPrefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, normalPrefix);

            HookedObjectContainer<StringHookedObjectTag<?>> hookedTags = objectTag.hookStrings(
                object, object, object, this
            );
            if(hookedTags != null){
                hookedTags.getHookedObjects().forEach(hooked ->{
                    tag.addAll(buildStringLists(
                        object, normalPrefix,
                        hooked.getPrefix(),
                        hooked.getObject()
                    ));
                });
            }
        });
        return tag;
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
