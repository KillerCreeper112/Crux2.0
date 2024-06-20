package killercreepr.crux.tags;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.container.LoreHookContainer;
import killercreepr.crux.tags.container.ObjectHookContainer;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.crux.tags.format.FormatPrefix;
import killercreepr.crux.tags.hook.lore.LoreHook;
import killercreepr.crux.tags.hook.string.StringHook;
import killercreepr.crux.tags.tag.LoreResolver;
import killercreepr.crux.tags.tag.ObjectTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class Tags {
    private final Collection<ObjectTag<?>> tags = new HashSet<>();

    //new
    public <T> @NotNull Collection<StringHook<T>> getTagsFrom(@NotNull T object){
        Collection<StringHook<T>> tags = new HashSet<>();
        this.tags.forEach(t ->{
            //todo if(t.canResolve(object)) tags.add((StringHook<T>) t);
        });
        return tags;
    }
    //new end

    public <T extends ObjectTag<?>> @NotNull T register(@NotNull T tag){
        tags.add(tag);
        return tag;
    }

    public @NotNull Collection<ObjectTag<?>> getTagsFromInfo(@NotNull DataExchange info){
        Collection<ObjectTag<?>> tags = new HashSet<>();
        info.getData().values().forEach(o -> {
            if(o == null) return;
            tags.addAll(getTagsFromObject(o));
        });
        return tags;
    }

    public <T>  @NotNull Collection<LoreHook<T>> getLoreHooksFromObject(@NotNull T object){
        Collection<LoreHook<T>> tags = new HashSet<>();
        for(ObjectTag<T> tag : getTagsFromObject(object)){
            Collection<LoreHook<T>> request = tag.requestLore(object, this);
            if(request != null) tags.addAll(request);
        }
        return tags;
    }

    public <T> @NotNull Collection<ObjectTag<T>> getTagsFromObject(@NotNull T object){
        Collection<ObjectTag<T>> tags = new HashSet<>();
        this.tags.forEach(t ->{
            if(t.canResolve(object)) tags.add((ObjectTag<T>) t);
        });
        return tags;
    }

    public @NotNull StringHookContainer hookAllTagResolvers(@NotNull TextParserContext context, @NotNull DataExchange info){
        return hookAllTagResolvers(context, info, null);
    }

    public @NotNull StringHookContainer hookAllTagResolvers(@NotNull TextParserContext context, @NotNull DataExchange info, @Nullable FormatPrefix prefix){
        StringHookContainer container = new StringHookContainer(context);
        for(Holder<Object> o : info.getData().values()){
            if(o == null) continue;
            container.putAll(hookStringResolvers(context, o, prefix));
        }
        return container;
    }

    public @NotNull LoreHookContainer hookAllLoreTags(@NotNull DataExchange info){
        return hookAllLoreTags(info, null);
    }

    public @NotNull LoreHookContainer hookAllLoreTags(@NotNull DataExchange info, @Nullable FormatPrefix prefix){
        LoreHookContainer container = new LoreHookContainer();
        for(Object o : info.getData().values()){
            if(o == null) continue;
            container.putAll(hookLoreTags(o, prefix));
        }
        return container;
    }

    /**
     * @return Requests the base TagResolvers from this object and any hooked objects it may have.
     */
    public @NotNull StringHookContainer hookStringResolvers(@NotNull TextParserContext context, @NotNull Holder<?> holder, @Nullable FormatPrefix prefix){
        StringHookContainer container = new StringHookContainer(context);
        Object object = holder.value();
        if(object == null) return container;
        for(ObjectTag<Object> tag : getTagsFromObject(object)){
            String pre = prefix == null ? tag.defaultPrefix().prefix(tag, object) : prefix.prefix(tag, object);
            Collection<StringHook<Object>> requested = tag.requestStrings(object, this);
            if(requested != null){
                for(StringHook<Object> hook : requested){
                    container.put(hook.hookObject(this, holder, pre));
                }
            }
            Collection<ObjectHookContainer<?>> hookContainerList = tag.hookStrings(object, this);
            if(hookContainerList != null){
                for(ObjectHookContainer<?> hookContainer : hookContainerList){
                    String prePrefix = prefix == null ? hookContainer.getObjectTag().defaultPrefix().hookedPrefix(tag, object, hookContainer) :
                            prefix.hookedPrefix(tag, object, hookContainer);
                    hookContainer.getHooks().forEach(hook -> container.put(hook.hookObject(this, hookContainer.getHolder(), prePrefix)));
                }
            }
        }
        return container;
    }

    /**
     * @return Requests the base LoreHooks from this object and any hooked objects it may have.
     */
    public @NotNull LoreHookContainer hookLoreTags(@NotNull Object object, @Nullable FormatPrefix prefix){
        LoreHookContainer container = new LoreHookContainer();
        for(ObjectTag<Object> tag : getTagsFromObject(object)){
            String pre = prefix == null ? tag.defaultPrefix().prefix(tag, object) : prefix.prefix(tag, object);
            Collection<LoreHook<Object>> requested = tag.requestLore(object, this);
            if(requested != null){
                for(LoreHook<Object> hook : requested){
                    container.put(pre + hook.identifier(), LoreResolver.generic(pre+hook.identifier(), object, hook));
                }
            }
            Map<Object, Collection<LoreHook<?>>> hookContainer = tag.hookLore(object, this);
            if(hookContainer != null){
                hookContainer.forEach((o, hooks) ->{
                    for(LoreHook<?> hook : hooks){
                        //todo prefix (not too sure what this means anymore)..
                        container.put(pre + hook.identifier(), LoreResolver.generic(pre+hook.identifier(), o, hook));
                    }
                });
            }
        }
        return container;
    }
}
