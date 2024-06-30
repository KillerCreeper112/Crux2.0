package killercreepr.crux.tags;

import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.TagResolver;
import org.bukkit.Bukkit;
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

    <T> @Nullable FormatPrefix buildPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags);
    <T> @Nullable FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> baseTag, @NotNull T baseObject, @NotNull HookedObjectTag<?, ?> tag);
}
