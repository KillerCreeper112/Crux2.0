package killercreepr.crux.tags.tag;

import killerceepr.crux.tags.hook.LoreHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface LoreResolver {
    static @NotNull LoreResolver generic(@NotNull String identifier, @Nullable List<String> result){
        return new LoreResolver() {
            @Override
            public boolean has(@NotNull String name) {
                return identifier.equalsIgnoreCase(name);
            }

            @Override
            public @Nullable List<String> resolve(@NotNull String[] args) {
                return result;
            }
        };
    }

    static @NotNull LoreResolver generic(@NotNull String identifier, @NotNull Object object, @NotNull LoreHook<?> hook){
        return new LoreResolver() {
            @Override
            public boolean has(@NotNull String name) {
                return identifier.equalsIgnoreCase(name);
            }

            @Override
            public @Nullable List<String> resolve(@NotNull String[] args) {
                return hook.parseObject(object, args);
            }
        };
    }

    boolean has(final @NotNull String name);
    @Nullable List<String> resolve(final @NotNull String[] args);
}
