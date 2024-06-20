package killercreepr.crux.tags;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class CruxTags implements TagParser {
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
            StringTagContainer tags = objectTag.requestStrings(object, this);
            FormatPrefix prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);

            tags = objectTag.hookStrings(object, this);
            prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildHookedPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);
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
            StringListTagContainer tags = objectTag.requestStringLists(object, this);
            FormatPrefix prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);

            tags = objectTag.hookStringLists(object, this);
            prefix = prefixBuilder == null ? objectTag.defaultPrefix() : prefixBuilder.buildHookedPrefix(objectTag, object, tags);
            tag.addAll(tags, prefix);
        });
        return tag;
    }
    @Override
    public <T> @Nullable MergedTagContainer buildTags(@NotNull T object){
        MultiTagContainer tag = new MultiTagContainer(this);
        locateTags(object).forEach(objectTag ->{
            tag.getStringTags().addAll(buildStringTags(object));
            tag.getStringListTags().addAll(buildStringListTags(object));
        });
        return tag;
    }
}
