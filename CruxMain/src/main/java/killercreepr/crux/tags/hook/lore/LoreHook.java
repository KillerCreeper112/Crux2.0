package killercreepr.crux.tags.hook.lore;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.hook.ObjectHook;
import killercreepr.crux.tags.hook.string.StringHook;
import killercreepr.crux.tags.tag.LoreResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class LoreHook <T> extends ObjectHook<T> {
    public static @NotNull LoreHook<Object> parsed(@NotNull String identifier, @NotNull List<String> value){
        return new LoreHook<>(Object.class) {
            @Override
            public @NotNull List<String> parse(@Nullable Object object, @NotNull FormatArgs args, @NotNull TextParserContext context) {
                return value;
            }

            @Override
            public @NotNull String identifier() {
                return identifier;
            }
        };
    }

    public LoreHook(@NotNull Class<T> object) {
        super(object);
    }
    public @Nullable List<String> parseObject(@NotNull Object o, @NotNull FormatArgs args, @NotNull TextParserContext context){
        return parse((T) o, args, context);
    }
    public abstract @Nullable List<String> parse(@NotNull T object, @NotNull FormatArgs args, @NotNull TextParserContext context);

    public @Nullable LoreResolver loreResolver(@NotNull T object, @NotNull String prefix, @NotNull TextParserContext context){
        return loreResolver(object, prefix, FormatArgs.empty(), context);
    }

    public @Nullable LoreResolver loreResolverObject(@NotNull Object object, @NotNull String prefix, @NotNull TextParserContext context){
        return loreResolver((T) object, prefix, context);
    }

    public @Nullable LoreResolver loreResolver(@NotNull T object, @NotNull String prefix, @NotNull FormatArgs args, @NotNull TextParserContext context){
        List<String> parsed = parse(object, args, context);
        if(parsed == null) return null;
        return LoreResolver.generic(prefix + identifier(), parsed);
    }

    public @Nullable LoreResolver loreResolverObject(@NotNull Object object, @NotNull String prefix, @NotNull FormatArgs args, @NotNull TextParserContext contextt){
        return loreResolver((T) object, prefix, args, contextt);
    }

    public interface ILoreHookBuilder<T>{
        @Nullable List<String> parse(@NotNull T object, @NotNull FormatArgs args, @NotNull TextParserContext context);
    }

    public static class Builder<T>{
        private final Class<T> object;
        private final Map<String, ILoreHookBuilder<T>> hooks = new HashMap<>();
        public Builder(@NotNull Class<T> object) {
            this.object = object;
        }

        public Builder<T> generic(@NotNull String id, @NotNull LoreHook.ILoreHookBuilder<T> x){
            hooks.put(id, x);
            return this;
        }

        public @NotNull Collection<LoreHook<T>> build(){
            Collection<LoreHook<T>> list = new HashSet<>();
            hooks.forEach((id, i) ->{
                list.add(new LoreHook<>(object) {
                    @Override
                    public @Nullable List<String> parse(@NotNull T object, @NotNull FormatArgs args, @NotNull TextParserContext context) {
                        return i.parse(object, args, context);
                    }

                    @Override
                    public @NotNull String identifier() {
                        return id;
                    }
                });
            });
            return list;
        }
    }
}
