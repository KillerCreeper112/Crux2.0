package killercreepr.crux.tags.ab.tags;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.ab.container.MergedTagContainer;
import killercreepr.crux.tags.ab.container.MultiTagContainer;
import killercreepr.crux.tags.ab.container.StringListTagContainer;
import killercreepr.crux.tags.ab.container.StringTagContainer;
import killercreepr.crux.tags.ab.context.FormatPrefix;
import killercreepr.crux.tags.ab.hook.ObjectTag;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class Tags {
    public static @NotNull StringResolver parsed(@NotNull String id, @NotNull String value){
        return new StringResolver() {
            @Override
            public @NotNull String identifier() {
                return id;
            }

            @Override
            public @NotNull String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
                return value;
            }
        };
    }

    protected final Collection<ObjectTag<?>> tags = new HashSet<>();

    public Collection<ObjectTag<?>> getTags() {
        return tags;
    }

    public <T> @NotNull Collection<ObjectTag<T>> locateTags(@NotNull T object){
        Collection<ObjectTag<T>> list = new HashSet<>();
        tags.forEach(tag ->{
            if(!tag.canResolve(object)) return;
            list.add((ObjectTag<T>) tag);
        });
        return list;
    }

    public <T> @Nullable StringTagContainer buildStringTags(@NotNull T object){
        return buildStringTags(object, null);
    }
    public <T> @Nullable StringTagContainer buildStringTags(@NotNull T object, @Nullable TagsPrefixBuilder prefixBuilder){
        StringTagContainer tag = new StringTagContainer(this);
        locateTags(object).forEach(objectTag ->{
            StringTagContainer tags = objectTag.requestStrings(object, this);
            FormatPrefix prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);

            tags = objectTag.hookStrings(object, this);
            prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildHookedPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);
        });
        return tag;
    }

    public <T> @Nullable StringListTagContainer buildStringListTags(@NotNull T object){
        return buildStringListTags(object, null);
    }
    public <T> @Nullable StringListTagContainer buildStringListTags(@NotNull T object, @Nullable TagsPrefixBuilder prefixBuilder){
        StringListTagContainer tag = new StringListTagContainer(this);
        locateTags(object).forEach(objectTag ->{
            StringListTagContainer tags = objectTag.requestStringLists(object, this);
            FormatPrefix prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);

            tags = objectTag.hookStringLists(object, this);
            prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildHookedPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);
        });
        return tag;
    }

    public <T> @Nullable MergedTagContainer buildTags(@NotNull T object){
        MultiTagContainer tag = new MultiTagContainer(this);
        locateTags(object).forEach(objectTag ->{
            tag.getStringTags().addAll(buildStringTags(object));
            tag.getStringListTags().addAll(buildStringListTags(object));
        });
        return tag;
    }
}
