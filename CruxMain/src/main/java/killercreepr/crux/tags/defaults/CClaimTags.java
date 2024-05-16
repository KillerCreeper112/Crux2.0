package killercreepr.crux.tags.defaults;

import killerceepr.crux.Crux;
import killerceepr.crux.data.Holder;
import killerceepr.crux.tags.Tags;
import killerceepr.crux.tags.container.StringHookContainer;
import killerceepr.crux.tags.format.FormatPrefix;
import killerceepr.crux.tags.hook.LoreHook;
import killerceepr.crux.tags.hook.StringHook;
import killerceepr.crux.tags.tag.ObjectTag;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CClaimTags {
    private final Crux plugin;
    private final Tags tags;
    private final ObjectTag<OfflinePlayer> OFFLINE_PLAYER_TAGS;

    public record TestBois(@NotNull String name){ }

    public CClaimTags(@NotNull Crux plugin, @NotNull Tags tags) {
        this.plugin = plugin;
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
                                .generic("name", (p, args) -> p.name())
                                .generic("bargo", (p,args) ->{
                                    if(args.length > 0) return args[0];
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
                                .generic("name", (p, args) -> p.isOnline() ? p.getPlayer().getName() : "Not Found")
                                .generic("uuid", (p, args) -> p.getUniqueId().toString())
                                .generic("health", (p, args) -> p.isOnline() ? p.getPlayer().getHealth()+"" : "0")
                                .generic("exp", (p, args) -> p.isOnline() ? p.getPlayer().getExp()+"" : "0")
                                .generic("level", (p, args) ->p.isOnline() ? p.getPlayer().getLevel()+"" : "0")
                                .generic("exp_to_level", (p, args) ->p.isOnline() ? p.getPlayer().getExpToLevel()+"" : "0")
                                .build();
                    }

                    @Override
                    public @NotNull Collection<LoreHook<OfflinePlayer>> requestLore(@NotNull OfflinePlayer object, @NotNull Tags tags) {
                        return new LoreHook.Builder<>(OfflinePlayer.class)
                                .generic("team_members", (p,args) ->{
                                    List<String> teamMembers = List.of(
                                            "<red>one", "<yellow>two", "thReee", "FoR",
                                            "<yellow>{{8+3+2}} <- <gray>That should be a parsed equation (8+3+2)",
                                            "<player_name>, <green><player_health>"
                                    );
                                    List<String> list = new ArrayList<>();
                                    teamMembers.forEach(m ->{
                                        StringHookContainer container = tags
                                                .hookStringResolvers(Holder.directObject(object), FormatPrefix.addonPlusHook("member_"));
                                        list.add(Crux.FORMAT.setPlaceholders(m, container));
                                    });
                                    return list;
                                })
                                .build();
                    }
                }
        );
    }
}
