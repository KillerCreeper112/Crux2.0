package killercreepr.crux.external.placeholderapi;

import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatParserContext;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

//todo test this out
public class TagsExpansionHook /*extends PlaceholderExpansion */{
    protected final @NotNull String identifier;
    protected final @NotNull Format format;

    public TagsExpansionHook(@NotNull String identifier, @NotNull Format format) {
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

    public @Nullable StringResolver findResolver(@NotNull StringTagContainer container,
                                                 @NotNull ObjectTag<OfflinePlayer> tag,
                                                 @NotNull String objectIdentifier,
                                                 @NotNull String hookIdentifier){
        for(StringResolver resolver : container){
            if(!noUnderscore(tag.defaultPrefix().buildPrefix(resolver)).equalsIgnoreCase(objectIdentifier)) continue;
            if(!noUnderscore(resolver.identifier()).equalsIgnoreCase(hookIdentifier)) continue;
            return resolver;
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
            StringTagContainer container = tag.requestStrings(player, format.tags());
            if(container != null){
                StringResolver resolver = findResolver(container, tag, objectIdentifier, hookIdentifier);
                if(resolver != null) return resolver.resolve(hookArgs, new FormatParserContext.Builder(format).viewer(player).build());
            }

            container = tag.hookStrings(player, format.tags());
            if(container != null){
                StringResolver resolver = findResolver(container, tag, objectIdentifier, hookIdentifier);
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
