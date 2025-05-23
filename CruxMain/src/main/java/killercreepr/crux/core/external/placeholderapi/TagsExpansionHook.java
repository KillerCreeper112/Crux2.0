package killercreepr.crux.core.external.placeholderapi;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.format.FormatArgs;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagsExpansionHook extends PlaceholderExpansion{
    protected final @NotNull String identifier;
    protected final @NotNull FormatSerializer format;

    public TagsExpansionHook(@NotNull String identifier, @NotNull FormatSerializer format) {
        this.identifier = identifier;
        this.format = format;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return "killercreepr";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    public @Nullable StringResolver findResolver(@NotNull TagContainer<?> container,
                                                 @NotNull ObjectTag<OfflinePlayer> tag,
                                                 @NotNull String objectIdentifier,
                                                 @NotNull String hookIdentifier){
        Bukkit.broadcastMessage(container.asMap().keySet() + " tags");
        return (StringResolver) container.get(objectIdentifier);

        /*for(TagResolver<?> resolver : container){
            if(!noUnderscore(tag.defaultPrefix().buildPrefix(resolver)).equalsIgnoreCase(objectIdentifier)) continue;
            if(!noUnderscore(resolver.identifier()).equalsIgnoreCase(hookIdentifier)) continue;
            return (StringResolver) resolver;
        }*/
        //return null;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        //<player_name> = %crux_player_name%
        //<player_max_health>
        //<claimer_max_claims_per_outpost> = %crux_claimer_maxclaimsperoutpost%
        if(player == null) return null;
        //<player_name> = %crux_player:name%
        //<player_attribute:attack_damage> = %crux_player:attribute:attack_damage%
        //<claimer_max_claims_per_outpost> = %crux_claimer:maxclaimsperoutpost%
        List<String> argsList = new ArrayList<>(Arrays.asList(params.split(":")));
        String objectIdentifier = argsList.get(0);
        argsList.removeFirst();
        /*String hookIdentifier = argsList.get(1);
        Bukkit.broadcastMessage(objectIdentifier + " ");
        Bukkit.broadcastMessage(hookIdentifier + " ");*/
        //Bukkit.broadcastMessage(Arrays.toString(rawArgs[0].split(":")) + "");
        StringTagContainer container = format.tags().buildStringTags(player);
        if(container==null) return null;

        StringResolver resolver = container.get(objectIdentifier);
        if(resolver==null) return null;

        FormatArgs hookArgs = new FormatArgs(argsList.toArray(new String[0]));

        if(true) return resolver.resolve(hookArgs, TextParserContext.builder().format(format).viewer(player).build());

        for(ObjectTag<OfflinePlayer> tag : format.tags().locateTags(player)){
            //test offline player tags
            /*TagContainer<StringResolver> container = tag.requestStrings(player, format.tags());
            if(container != null){
                StringResolver resolver = findResolver(container, tag, objectIdentifier, null);
                if(resolver != null) return resolver.resolve(hookArgs, new FormatParserContext.Builder(format).viewer(player).build());
            }

            HookedObjectContainer<StringHookedObjectTag<?>> hookedContainer = tag.hookStrings(player, format.tags());
            if(hookedContainer != null){
                StringResolver resolver = findResolver(hookedContainer.toTags(format.tags()), tag, objectIdentifier, null);
                if(resolver != null) return resolver.resolve(hookArgs, new FormatParserContext.Builder(format).viewer(player).build());
            }*/

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
        return text;//.replace("_", "");
    }
}
