package killercreepr.crux.core.text.tags.standard.object;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
import killercreepr.crux.core.text.resolver.Tag;
import net.kyori.adventure.key.Key;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public class OfflinePlayerTags implements ObjectTag<OfflinePlayer> {
    @Override
    public @NotNull Class<OfflinePlayer> getObjectType() {
        return OfflinePlayer.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("player_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull OfflinePlayer p, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("name", (args, context) -> p.getName() + ""))
            .add(Tag.string("display_name", (args, context) ->{
                Player online = p.getPlayer();
                if(online==null) return "not online";
                return context.getFormat().serialize(online.displayName());
            }))
            .add(Tag.string("world", (args, context) ->{
                Player online = p.getPlayer();
                if(online==null) return "not online";
                return online.getWorld().getName();
            }))
            .add(Tag.string("gamemode", (args, context) ->{
                Player online = p.getPlayer();
                if(online==null) return "not online";
                return online.getGameMode().toString().toLowerCase();
            }))
            .add(Tag.string("ping", (args, context) -> p.isOnline()?p.getPlayer().getPing()+"" :"0"))
            .add(Tag.string("uuid", (args, context) -> p.getUniqueId().toString()))
            .add(Tag.string("health", (args, context) -> p.isOnline() ? p.getPlayer().getHealth()+"" : "0"))
            .add(Tag.string("exp", (args, context) -> p.isOnline() ? p.getPlayer().getExp()+"" : "0"))
            .add(Tag.string("total_exp", (args, context) -> p.isOnline() ? p.getPlayer().getTotalExperience()+"" : "0"))
            .add(Tag.string("level", (args, context) ->p.isOnline() ? p.getPlayer().getLevel()+"" : "0"))
            .add(Tag.string("exp_to_level", (args, context) ->p.isOnline() ? p.getPlayer().getExpToLevel()+"" : "0"))
            .add(Tag.string("no_damage_ticks", (args, context) -> p.isOnline()?p.getPlayer().getNoDamageTicks()+"":"0"))
            .add(Tag.string("is_online", (args, ctx) -> p.isOnline() + ""))
            .add(Tag.string("attribute", (args, context) ->{
                Player online = p.getPlayer();
                if(online==null) return "not online";
                //<player_attribute:(attribute):[value_to_get{value, default, base},default=value]>
                NamespacedKey key = NamespacedKey.fromString(args.get(0));
                if(key == null) return "invalid key - " + args.get(0);
                Attribute attribute = Registry.ATTRIBUTE.get(key);
                if(attribute == null) return "attribute not found- " + key;

                AttributeInstance instance = online.getAttribute(attribute);
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
            }))
            .add(Tag.string("equipment", (args, context) ->{
                Player online = p.getPlayer();
                if(online==null) return "not online";
                EquipmentSlot slot = EquipmentSlot.valueOf(args.get(0).toUpperCase());
                ItemStack item = online.getInventory().getItem(slot);
                if(item.isEmpty()) return item.getType().key().asString();
                return Base64.getEncoder().encodeToString(item.serializeAsBytes());
            }))
            .add(Tag.string("has_permission", (args, ctx) ->{
                Player online = p.getPlayer();
                if(online==null) return "false";
                String permission = args.get(0);
                return online.hasPermission(permission) + "";
            }))
            .add(Tag.string("has_potion_effect", (args, ctx) ->{
                Player online = p.getPlayer();
                if(online==null) return "false";
                String potionName = args.get(0);
                PotionEffectType type = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(Key.key(potionName));
                if(type == null) return potionName + " not found";
                return online.hasPotionEffect(type) + "";
            }))
            .add(Tag.string("has_cooldown", (args, ctx) ->{
                Player online = p.getPlayer();
                if(online==null) return "false";
                String materialName = args.get(0);
                Material type = Registry.MATERIAL.get(Key.key(materialName));
                if(type == null) return materialName + " not found";
                return online.hasCooldown(type) + "";
            }))
            .add(Tag.string("has_resourcepack", (args, ctx) ->{
                Player online = p.getPlayer();
                if(online==null) return "false";
                return online.hasResourcePack() + "";
            }))
            .add(Tag.string("is_op", (args, ctx) -> p.isOp() + ""))
            .add(Tag.string("stat", (args, ctx) ->{
                Key key = Key.key(args.get(0));
                Statistic statistic = getStat(key);
                if(statistic == null) return key + " statistic not found";
                return p.getStatistic(statistic) + "";
            }))
            .add(Tag.string("stat_material", (args, ctx) ->{
                Key key = Key.key(args.get(0));
                Key materialKey = Key.key(args.get(1));
                Material material = Registry.MATERIAL.get(materialKey);
                if(material == null) return materialKey + " material not found";
                Statistic statistic = getStat(key);
                if(statistic == null) return key + " statistic not found";
                return p.getStatistic(statistic, material) + "";
            }))
            .add(Tag.string("stat_entity", (args, ctx) ->{
                Key key = Key.key(args.get(0));
                Key entityKey = Key.key(args.get(1));
                EntityType entityType = Registry.ENTITY_TYPE.get(entityKey);
                if(entityType == null) return entityKey + " entity type not found";
                Statistic statistic = getStat(key);
                if(statistic == null) return key + " statistic not found";
                return p.getStatistic(statistic, entityType) + "";
            }))
            .add(Tag.string("first_played", (args, ctx) ->{
                return p.getFirstPlayed() + "";
            }))
            ;
    }

    public static Statistic getStat(Key key){
        Statistic statistic = Registry.STATISTIC.get(key);
        if(statistic != null) return statistic;
        if(Key.key("play_one_tick").equals(key)) return Statistic.PLAY_ONE_MINUTE;
        if(Key.key("play_time_ticks").equals(key)) return Statistic.PLAY_ONE_MINUTE;
        if(Key.key("play_time").equals(key)) return Statistic.PLAY_ONE_MINUTE;
        return null;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull OfflinePlayer object, @NotNull TagParser tags) {
        HookedObjectContainer<StringHookedObjectTag<?>> hooks = HookedObjectContainer.string();
        Player p = object.getPlayer();
        if(p != null){
            hooks.addAll(tags.hookStrings(p.getWorld(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("player_world/")
            )));
        }
        return hooks;
    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull OfflinePlayer object, @NotNull TagParser tags) {
        HookedObjectContainer<StringListHookedObjectTag<?>> hooks = HookedObjectContainer.stringList();
        Player p = object.getPlayer();
        if(p != null){
            hooks.addAll(tags.hookStringLists(p.getWorld(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("player_world/")
            )));
        }
        return hooks;
    }
}
