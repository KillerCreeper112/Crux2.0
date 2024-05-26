package killercreepr.crux.tags.hook;

import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.FormatContext;
import killercreepr.crux.tags.tag.LoreResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class LoreHook <T> extends ObjectHook<T>{
    public LoreHook(@NotNull Class<T> object) {
        super(object);
    }
    public @Nullable List<String> parseObject(@NotNull Object o, @NotNull FormatArgs args, @NotNull FormatContext context){
        return parse((T) o, args, context);
    }
    public abstract @Nullable List<String> parse(@NotNull T object, @NotNull FormatArgs args, @NotNull FormatContext context);

    public @Nullable LoreResolver loreResolver(@NotNull T object, @NotNull String prefix, @NotNull FormatContext context){
        return loreResolver(object, prefix, FormatArgs.empty(), context);
    }

    public @Nullable LoreResolver loreResolverObject(@NotNull Object object, @NotNull String prefix, @NotNull FormatContext context){
        return loreResolver((T) object, prefix, context);
    }

    public @Nullable LoreResolver loreResolver(@NotNull T object, @NotNull String prefix, @NotNull FormatArgs args, @NotNull FormatContext context){
        List<String> parsed = parse(object, args, context);
        if(parsed == null) return null;
        return LoreResolver.generic(prefix + identifier(), parsed);
    }

    public @Nullable LoreResolver loreResolverObject(@NotNull Object object, @NotNull String prefix, @NotNull FormatArgs args, @NotNull FormatContext contextt){
        return loreResolver((T) object, prefix, args, contextt);
    }

    public interface ILoreHookBuilder<T>{
        @Nullable List<String> parse(@NotNull T object, @NotNull FormatArgs args, @NotNull FormatContext context);
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
                    public @Nullable List<String> parse(@NotNull T object, @NotNull FormatArgs args, @NotNull FormatContext context) {
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
