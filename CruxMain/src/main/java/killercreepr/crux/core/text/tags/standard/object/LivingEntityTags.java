package killercreepr.crux.core.text.tags.standard.object;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.resolver.Tag;
import net.kyori.adventure.key.Key;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public class LivingEntityTags implements ObjectTag<LivingEntity> {
    @Override
    public @NotNull Class<LivingEntity> getObjectType() {
        return LivingEntity.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("living_entity_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull LivingEntity p, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("uuid", (args, context) -> p.getUniqueId().toString()))
            .add(Tag.string("health", (args, context) -> p.getHealth() + ""))
            .add(Tag.string("no_damage_ticks", (args, context) -> p.getNoDamageTicks() + ""))
            .add(Tag.string("attribute", (args, context) ->{
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
            }))
            .add(Tag.string("equipment", (args, context) ->{
                EntityEquipment equip = p.getEquipment();
                if(equip == null) return "minecraft:air";
                EquipmentSlot slot = EquipmentSlot.valueOf(args.get(0).toUpperCase());
                ItemStack item = equip.getItem(slot);
                if(item.isEmpty()) return item.getType().key().asString();
                return Base64.getEncoder().encodeToString(item.serializeAsBytes());
            }))
            .add(Tag.string("has_potion_effect", (args, ctx) ->{
                String potionName = args.get(0);
                PotionEffectType type = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(Key.key(potionName));
                if(type == null) return potionName + " not found";
                return p.hasPotionEffect(type) + "";
            }))
            .add(Tag.string("is_op", (args, ctx) -> p.isOp() + ""))
            ;
    }
    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull LivingEntity p, Object base, Object parent, @NotNull TagParser tags) {
        HookedObjectContainer<StringHookedObjectTag<?>> hooks = HookedObjectContainer.string();
        hooks.addAll(tags.hookStrings(
            p.getWorld(), base, p, FormatPrefix.simple("world/")
        ));
        return hooks;
    }
}
