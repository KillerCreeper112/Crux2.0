package killercreepr.crux.tags.hook;

import killerceepr.crux.tags.tag.LoreResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class LoreHook <T> extends ObjectHook<T>{
    public LoreHook(@NotNull Class<T> object) {
        super(object);
    }
    public @Nullable List<String> parseObject(@NotNull Object o, @NotNull String[] args){
        return parse((T) o, args);
    }
    public abstract @Nullable List<String> parse(@NotNull T object, @NotNull String[] args);

    public @Nullable LoreResolver loreResolver(@NotNull T object, @NotNull String prefix){
        return loreResolver(object, prefix, new String[0]);
    }

    public @Nullable LoreResolver loreResolverObject(@NotNull Object object, @NotNull String prefix){
        return loreResolver((T) object, prefix);
    }

    public @Nullable LoreResolver loreResolver(@NotNull T object, @NotNull String prefix, @NotNull String[] args){
        List<String> parsed = parse(object, args);
        if(parsed == null) return null;
        return LoreResolver.generic(prefix + identifier(), parsed);
    }

    public @Nullable LoreResolver loreResolverObject(@NotNull Object object, @NotNull String prefix, @NotNull String[] args){
        return loreResolver((T) object, prefix, args);
    }

    public interface ILoreHookBuilder<T>{
        @Nullable List<String> parse(@NotNull T object, @NotNull String[] args);
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
                list.add(new LoreHook<T>(object) {
                    @Override
                    public @Nullable List<String> parse(@NotNull T object, @NotNull String[] args) {
                        return i.parse(object, args);
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
