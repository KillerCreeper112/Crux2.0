package killercreepr.crux.core.external.placeholderapi;

import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.resolver.TagResolver;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.text.format.FormatParserContext;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

//todo test this out
public class TagsExpansionHook /*extends PlaceholderExpansion */{
    protected final @NotNull String identifier;
    protected final @NotNull FormatSerializer format;

    public TagsExpansionHook(@NotNull String identifier, @NotNull FormatSerializer format) {
        this.identifier = identifier;
        this.format = format;
    }

    //@Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    //@Override
    public @NotNull String getAuthor() {
        return "killercreepr";
    }

    //@Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    public @Nullable StringResolver findResolver(@NotNull TagContainer<?> container,
                                                 @NotNull ObjectTag<OfflinePlayer> tag,
                                                 @NotNull String objectIdentifier,
                                                 @NotNull String hookIdentifier){
        for(TagResolver<?> resolver : container){
            if(!noUnderscore(tag.defaultPrefix().buildPrefix(resolver)).equalsIgnoreCase(objectIdentifier)) continue;
            if(!noUnderscore(resolver.identifier()).equalsIgnoreCase(hookIdentifier)) continue;
            return (StringResolver) resolver;
        }
        return null;
    }

    //@Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        //<player_name> = %crux_player_name%
        //<player_max_health>
        //<claimer_max_claims_per_outpost> = %crux_claimer_maxclaimsperoutpost%
        if(player == null) return null;
        List<String> argsList = new ArrayList<>(List.of(params.split("_")));
        String objectIdentifier = argsList.get(0);
        String hookIdentifier = argsList.get(1);
        argsList.remove(0);
        argsList.remove(0);
        FormatArgs hookArgs = new FormatArgs(argsList.toArray(new String[0]));
        for(ObjectTag<OfflinePlayer> tag : format.tags().locateTags(player)){
            //test offline player tags
            TagContainer<StringResolver> container = tag.requestStrings(player, format.tags());
            if(container != null){
                StringResolver resolver = findResolver(container, tag, objectIdentifier, hookIdentifier);
                if(resolver != null) return resolver.resolve(hookArgs, new FormatParserContext.Builder(format).viewer(player).build());
            }

            HookedObjectContainer<StringHookedObjectTag<?>> hookedContainer = tag.hookStrings(player, format.tags());
            if(hookedContainer != null){
                StringResolver resolver = findResolver(hookedContainer.toTags(format.tags()), tag, objectIdentifier, hookIdentifier);
                if(resolver != null) return resolver.resolve(hookArgs, new FormatParserContext.Builder(format).viewer(player).build());
            }

            /*if(noUnderscore(tag.defaultPrefix().prefix(tag, player)).equalsIgnoreCase(objectIdentifier)){
                Collection<StringHook<OfflinePlayer>> tagHooks = tag.requestStrings(player, format.getTags());
                if(tagHooks != null){
                    for(StringHook<OfflinePlayer> hook : tagHooks){
                        //found hook to parse!
                        if(noUnderscore(hook.identifier()).equalsIgnoreCase(hookIdentifier)){
                            return hook.parse(player, hookArgs, new FormatParserContext.Builder(format).viewer(player).build());
                        }
                    }
                }
                continue;
            }
            //test hooks
            Collection<ObjectHookContainer<?>> hookedTags = tag.hookStrings(player, format.getTags());
            if(hookedTags == null) continue;
            for(ObjectHookContainer<?> hookContainer : hookedTags){
                for(StringHook<?> hook : hookContainer.getHooks()){
                    //found a hooked hook (lol)!
                    if(noUnderscore(hook.identifier()).equalsIgnoreCase(hookIdentifier)){
                        Object value = hookContainer.getHolder().value();
                        if(value == null) continue;
                        return hook.parseObject(value, hookArgs, new FormatParserContext.Builder(format)
                                .viewer(player)
                                .build());
                    }
                }
            }*/
        }
        return null;
        //return super.onRequest(player, params);
    }

    private @NotNull String noUnderscore(@NotNull String text){
        return text.replace("_", "");
    }
}
