package killercreepr.crux.tags.defaults;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.crux.tags.format.FormatPrefix;
import killercreepr.crux.tags.hook.LoreHook;
import killercreepr.crux.tags.hook.StringHook;
import killercreepr.crux.tags.tag.ObjectTag;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CClaimTags {
    private final Tags tags;
    private final ObjectTag<OfflinePlayer> OFFLINE_PLAYER_TAGS;

    public record TestBois(@NotNull String name){ }

    public CClaimTags(@NotNull Tags tags) {
        this.tags = tags;

        tags.register(
                new ObjectTag<>(TestBois.class) {

                    @Override
                    public @NotNull FormatPrefix defaultPrefix() {
                        return FormatPrefix.generic("test_");
                    }

                    @Override
                    public @NotNull Collection<StringHook<TestBois>> requestStrings(@NotNull TestBois object, @NotNull Tags tags) {
                        return new StringHook.Builder<>(TestBois.class)
                                .generic("name", (p, args, context) -> p.name())
                                .generic("bargo", (p,args, context) ->{
                                    if(args.getArgs().length > 0) return args.getArgs()[0];
                                    return "bargo";
                                })
                                .build();
                    }
                }
        );

        OFFLINE_PLAYER_TAGS = tags.register(
                new ObjectTag<>(OfflinePlayer.class) {
                    @Override
                    public @NotNull FormatPrefix defaultPrefix() {
                        return FormatPrefix.generic("player_");
                    }

                    @Override
                    public @NotNull Collection<StringHook<OfflinePlayer>> requestStrings(@NotNull OfflinePlayer object, @NotNull Tags tags) {
                        return new StringHook.Builder<>(OfflinePlayer.class)
                                .generic("name", (p, args, context) -> p.isOnline() ? p.getPlayer().getName() : "Not Found")
                                .generic("uuid", (p, args, context) -> p.getUniqueId().toString())
                                .generic("health", (p, args, context) -> p.isOnline() ? p.getPlayer().getHealth()+"" : "0")
                                .generic("exp", (p, args, context) -> p.isOnline() ? p.getPlayer().getExp()+"" : "0")
                                .generic("level", (p, args, context) ->p.isOnline() ? p.getPlayer().getLevel()+"" : "0")
                                .generic("exp_to_level", (p, args, context) ->p.isOnline() ? p.getPlayer().getExpToLevel()+"" : "0")
                                .build();
                    }

                    @Override
                    public @NotNull Collection<LoreHook<OfflinePlayer>> requestLore(@NotNull OfflinePlayer object, @NotNull Tags tags) {
                        return new LoreHook.Builder<>(OfflinePlayer.class)
                                .generic("team_members", (p, args, context) ->{
                                    List<String> teamMembers = List.of(
                                            "<red>one", "<yellow>two", "thReee", "FoR",
                                            "<yellow>{{8+3+2}} <- <gray>That should be a parsed equation (8+3+2)",
                                            "<player_name>, <green><player_health>"
                                    );
                                    List<String> list = new ArrayList<>();
                                    teamMembers.forEach(m ->{
                                        StringHookContainer container = tags
                                                .hookStringResolvers(context, Holder.directObject(object), FormatPrefix.addonPlusHook("member_"));
                                        list.add(context.getFormat().setPlaceholders(m, container));
                                    });
                                    return list;
                                })
                                .build();
                    }
                }
        );
    }
}
