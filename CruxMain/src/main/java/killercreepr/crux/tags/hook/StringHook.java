package killercreepr.crux.tags.hook;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.Tags;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class StringHook<T> extends ObjectHook<T>{
    public StringHook(@NotNull Class<T> object) {
        super(object);
    }
    public @Nullable String parseObject(@NotNull Object object, @NotNull String[] args){
        return parse((T) object, args);
    }

    public abstract @Nullable String parse(@NotNull T object, @NotNull String[] args);

    public @Nullable String parse(@NotNull T object, @NotNull String[] args, @NotNull Tags tags){
        return parse(object, args);
    }

    public @NotNull StringHookedObject<T> hook(@NotNull Tags tags, @NotNull Holder<T> holder, @NotNull String prefix){
        return new StringHookedObject<>(tags, prefix, holder, this);
    }

    public @NotNull StringHookedObject<?> hookObject(@NotNull Tags tags, @NotNull Holder<?> holder, @NotNull String prefix){
        return new StringHookedObject<>(tags, prefix, (Holder<T>) holder, this);
    }

    /**
     * @return Builds a MiniMessage TagResolver for this hook.
     */
    public @Nullable TagResolver tagResolver(@NotNull T object, @NotNull String prefix){
        return new TagResolver() {
            @Override
            public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
                if(!has(name)) return null;
                List<String> args = new ArrayList<>();
                while(arguments.hasNext()){
                    args.add(arguments.pop().value());
                }
                String output = parse(object, args.toArray(new String[0]));
                return output == null ? null : Tag.preProcessParsed(output);
            }

            @Override
            public boolean has(@NotNull String name) {
                return (prefix + identifier()).equalsIgnoreCase(name);
            }
        };
    }

    public @Nullable TagResolver tagResolverObject(@NotNull Object object, @NotNull String prefix){
        return tagResolver((T) object, prefix);
    }

    public interface IStringHookBuilder<T>{
        @Nullable String parse(@NotNull T object, @NotNull String[] args);
    }

    public static class Builder<T>{
        private final Class<T> object;
        private final Map<String, IStringHookBuilder<T>> hooks = new HashMap<>();
        private final Collection<StringHook<T>> rawHooks = new HashSet<>();
        public Builder(@NotNull Class<T> object) {
            this.object = object;
        }

        public Builder<T> generic(@NotNull String id, @NotNull IStringHookBuilder<T> x){
            hooks.put(id, x);
            return this;
        }

        public Builder<T> add(@NotNull StringHook<T> h){
            rawHooks.add(h);
            return this;
        }

        public @NotNull Collection<StringHook<T>> build(){
            Collection<StringHook<T>> list = new HashSet<>();
            hooks.forEach((id, i) ->{
                list.add(new StringHook<T>(object) {
                    @Override
                    public @Nullable String parse(@NotNull T object, @NotNull String[] args) {
                        return i.parse(object, args);
                    }

                    @Override
                    public @NotNull String identifier() {
                        return id;
                    }
                });
            });
            list.addAll(rawHooks);
            return list;
        }
    }

    /*public static class HookedBuilder{
        private final Collection<ObjectHookContainer<?>> hooks = new HashSet<>();
        public HookedBuilder add(@NotNull Collection<ObjectHookContainer<Object>> hooks){
            this.hooks.addAll(hooks);
            return this;
        }

        public HookedBuilder add(@NotNull Object object, @NotNull Tags tags){
            Collection<ObjectHookContainer<Object>> c = tags.getStringHooksFromObject(object);
            this.hooks.addAll(c);
            return this;
        }

        public @NotNull Collection<ObjectHookContainer<?>> build(){
            return hooks;
        }
    }*/
}
