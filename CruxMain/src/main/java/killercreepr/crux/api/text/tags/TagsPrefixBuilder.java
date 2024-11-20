package killercreepr.crux.api.text.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectTag;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.TagResolver;
import killercreepr.crux.api.text.tags.container.TagContainer;
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
            public <T> @NotNull FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> baseTag, @NotNull T baseObject, @NotNull HookedObjectTag<?, ?> tag) {
                return new FormatPrefix() {
                    @Override
                    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                        return prefix +
                            baseTag.defaultPrefix().buildPrefix(resolver) +
                            tag.getObjectTag().defaultPrefix().buildPrefix(resolver);
                    }
                };
            }
        };
    }

    static @NotNull TagsPrefixBuilder addon(@NotNull String prefix){
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
            public <T> @NotNull FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> baseTag, @NotNull T baseObject, @NotNull HookedObjectTag<?, ?> tag) {
                return new FormatPrefix() {
                    @Override
                    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                        return prefix + tag.getObjectTag().defaultPrefix().buildPrefix(resolver);
                    }
                };
            }
        };
    }

    static @NotNull TagsPrefixBuilder overwriteBase(@NotNull String prefix){
        return new TagsPrefixBuilder() {
            @Override
            public <T> @NotNull FormatPrefix buildPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags) {
                return new FormatPrefix() {
                    @Override
                    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                        return prefix;
                    }
                };
            }

            @Override
            public <T> @NotNull FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> baseTag, @NotNull T baseObject, @NotNull HookedObjectTag<?, ?> tag) {
                return new FormatPrefix() {
                    @Override
                    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                        return prefix + tag.getObjectTag().defaultPrefix().buildPrefix(resolver);
                    }
                };
            }
        };
    }

    <T> @Nullable FormatPrefix buildPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags);
    <T> @Nullable FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> baseTag, @NotNull T baseObject, @NotNull HookedObjectTag<?, ?> tag);
}
