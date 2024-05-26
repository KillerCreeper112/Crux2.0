package killercreepr.crux.external.placeholderapi;

import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.container.ObjectHookContainer;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.hook.StringHook;
import killercreepr.crux.tags.tag.ObjectTag;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TagsExpansionHook extends PlaceholderExpansion {
    protected final @NotNull String identifier;
    protected final @NotNull Format format;

    public TagsExpansionHook(@NotNull String identifier, @NotNull Format format) {
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
        for(ObjectTag<OfflinePlayer> tag : format.getTags().getTagsFromObject(player)){
            //test offline player tags
            if(noUnderscore(tag.defaultPrefix().prefix(tag, player)).equalsIgnoreCase(objectIdentifier)){
                Collection<StringHook<OfflinePlayer>> tagHooks = tag.requestStrings(player, format.getTags());
                if(tagHooks != null){
                    for(StringHook<OfflinePlayer> hook : tagHooks){
                        //found hook to parse!
                        if(noUnderscore(hook.identifier()).equalsIgnoreCase(hookIdentifier)){
                            return hook.parse(player, hookArgs, format.buildContext());
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
                        return hook.parseObject(value, hookArgs, format.buildContext());
                    }
                }
            }
        }
        return super.onRequest(player, params);
    }

    private @NotNull String noUnderscore(@NotNull String text){
        return text.replace("_", "");
    }
}
