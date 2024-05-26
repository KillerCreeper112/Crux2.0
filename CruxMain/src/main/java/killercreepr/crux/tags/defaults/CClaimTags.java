package killercreepr.crux.tags.defaults;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.crux.tags.format.FormatPrefix;
import killercreepr.crux.tags.hook.LoreHook;
import killercreepr.crux.tags.hook.StringHook;
import killercreepr.crux.tags.tag.ObjectTag;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
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
                                .generic("name", (p, args, context) -> p.getName() + "")
                                .generic("display_name", (p, args, context) ->{
                                    Player online = p.getPlayer();
                                    if(online==null) return "not online";
                                    return context.getFormat().serialize(online.displayName());
                                })
                                .generic("world", (p, args, context) ->{
                                    Player online = p.getPlayer();
                                    if(online==null) return "not online";
                                    return online.getWorld().getName();
                                })
                                .generic("gamemode", (p, args, context) ->{
                                    Player online = p.getPlayer();
                                    if(online==null) return "not online";
                                    return online.getGameMode().toString().toLowerCase();
                                })
                                .generic("ping", (p, args, context) -> p.isOnline()?p.getPlayer().getPing()+"" :"0")
                                .generic("uuid", (p, args, context) -> p.getUniqueId().toString())
                                .generic("health", (p, args, context) -> p.isOnline() ? p.getPlayer().getHealth()+"" : "0")
                                .generic("exp", (p, args, context) -> p.isOnline() ? p.getPlayer().getExp()+"" : "0")
                                .generic("total_exp", (p, args, context) -> p.isOnline() ? p.getPlayer().getTotalExperience()+"" : "0")
                                .generic("level", (p, args, context) ->p.isOnline() ? p.getPlayer().getLevel()+"" : "0")
                                .generic("exp_to_level", (p, args, context) ->p.isOnline() ? p.getPlayer().getExpToLevel()+"" : "0")
                                .generic("no_damage_ticks", (p, args, context) -> p.isOnline()?p.getPlayer().getNoDamageTicks()+"":"0")
                                .generic("attribute", (player, args, context) ->{
                                    Player p = player.getPlayer();
                                    if(p==null) return "not online";
                                    //<player_attribute:(attribute):[value_to_get{value, default, base},default=value]>
                                    NamespacedKey key = NamespacedKey.fromString(args.get(0));
                                    if(key == null) return "invalid key - " + args.get(0);
                                    Attribute attribute = Registry.ATTRIBUTE.get(key);
                                    if(attribute == null) return "attribute not found- " + key;

                                    AttributeInstance instance = p.getAttribute(attribute);
                                    if(instance==null) return "0";

                                    String check = args.getOrDefault(1, "value").toLowerCase();
                                    double x;
                                    switch (check){
                                        case "value" -> x = instance.getValue();
                                        case "default" -> x = instance.getDefaultValue();
                                        case "base" -> x = instance.getBaseValue();
                                        default -> {
                                            return "invalid arg- " + check + " valid={value, default, base}";
                                        }
                                    }
                                    return Double.toString(x);
                                })
                                .generic("equipment", (player, args, context) ->{
                                    Player p = player.getPlayer();
                                    if(p==null) return "not online";
                                    EquipmentSlot slot = EquipmentSlot.valueOf(args.get(0).toUpperCase());
                                    ItemStack item = p.getInventory().getItem(slot);
                                    return item.getType().toString().toLowerCase();
                                })
                                .build();
                    }

                    @Override
                    public @NotNull Collection<LoreHook<OfflinePlayer>> requestLore(@NotNull OfflinePlayer object, @NotNull Tags tags) {
                        return new LoreHook.Builder<>(OfflinePlayer.class)
                                .generic("team_members", (p, args, context) ->{
                                    List<String> teamMembers = List.of(
                                            "<red>one", "<yellow>two", "thReee", "FoR",
                                            "<yellow>{{8+3+2 + <player_health>}} <- <gray>That should be a parsed equation (8+3+2+<player_health>)",
                                            "<player_name>, <green><player_health>"
                                    );
                                    List<String> list = new ArrayList<>();
                                    teamMembers.forEach(m ->{
                                        StringHookContainer container = tags
                                                .hookStringResolvers(context, Holder.directObject(object), null);
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
