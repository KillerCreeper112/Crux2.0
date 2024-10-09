package killercreepr.crux.tags.standard.object;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectContainer;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.hook.impl.StringHookedObjectTag;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import killercreepr.crux.tags.resolver.Tag;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
                return item.getType().toString().toLowerCase();
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
            ;
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
