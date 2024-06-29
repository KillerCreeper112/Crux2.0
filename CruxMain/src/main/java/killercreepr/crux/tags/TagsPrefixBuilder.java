package killercreepr.crux.tags;

import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagsPrefixBuilder {
    static @NotNull TagsPrefixBuilder addonPlusHook(@NotNull String prefix){
        return new TagsPrefixBuilder() {
            @Override
            public <T> @NotNull FormatPrefix buildPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags) {
                return new FormatPrefix() {
                    @Override
                    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                        return prefix + tag.defaultPrefix().buildPrefix(resolver);
                    }
                };
            }

            @Override
            public @Nullable <T> FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags) {
                return TagsPrefixBuilder.super.buildHookedPrefix(tag, object, tags);
            }

            /*@Override
            public @NotNull String prefix(@NotNull ObjectTag<?> tag, @NotNull Object object) {
                return prefix + tag.defaultPrefix().prefix(tag, object);
            }

            @Override
            public @NotNull String hookedPrefix(@NotNull ObjectTag<?> mainTag, @NotNull Object object, @NotNull ObjectHookContainer<?> hooked) {
                return prefix + mainTag.defaultPrefix().prefix(mainTag, object) +
                    hooked.getObjectTag().defaultPrefix().prefix(hooked.getObjectTag(), hooked.getObject());
            }*/
        };
    }

    static @NotNull TagsPrefixBuilder addon(@NotNull String prefix){
        return new TagsPrefixBuilder() {
            @Override
            public @Nullable <T> FormatPrefix buildPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags) {
                return new FormatPrefix() {
                    @Override
                    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                        return prefix + tag.defaultPrefix().buildPrefix(resolver);
                    }
                };
            }

        };
    }

    static @NotNull TagsPrefixBuilder addon(@NotNull String prefix) {
        return new FormatPrefix() {
            @Override
            public @NotNull String prefix(@NotNull ObjectTag<?> tag, @NotNull Object object) {
                return prefix + tag.defaultPrefix().prefix(tag, object);
            }

            @Override
            public @NotNull String hookedPrefix(@NotNull ObjectTag<?> mainTag, @NotNull Object object, @NotNull ObjectHookContainer<?> hooked) {
                return prefix +
                    hooked.getObjectTag().defaultPrefix().prefix(hooked.getObjectTag(), hooked.getObject());
            }
        };
    }

    <T> @Nullable FormatPrefix buildPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags);
    default <T> @Nullable FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> baseTag, @NotNull T baseObject, @NotNull HookedObjectTag<?, ?> tag){
        return buildPrefix(baseTag, baseObject, tag.getTags());
    }
}
