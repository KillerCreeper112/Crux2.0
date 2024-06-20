package killercreepr.crux.tags.tag;

import killercreepr.crux.tags.ab.resolver.TagResolver;
import killercreepr.crux.tags.hook.lore.LoreHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface LoreResolver extends TagResolver<List<String>> {
    static @NotNull LoreResolver generic(@NotNull String identifier, @Nullable List<String> result){
        return null;//todo
        /*return new LoreResolver() {
            @Override
            public @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
                return result;
            }
        };*/
    }

    static @NotNull LoreResolver generic(@NotNull String identifier, @NotNull Object object, @NotNull LoreHook<?> hook){
        return null;//todo
        /*return new LoreResolver() {
            @Override
            public boolean has(@NotNull String name) {
                return identifier.equalsIgnoreCase(name);
            }

            @Override
            public @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
                return hook.parseObject(object, args, context);
            }
        };*/
    }

    /*boolean has(final @NotNull String name);
    @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext context);*/
}
