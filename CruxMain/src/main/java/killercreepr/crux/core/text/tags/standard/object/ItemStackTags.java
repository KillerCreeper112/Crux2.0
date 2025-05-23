package killercreepr.crux.core.text.tags.standard.object;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.MapItemColor;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.crux.core.util.CruxString;
import net.kyori.adventure.key.Key;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public class ItemStackTags implements ObjectTag<ItemStack> {
    @Override
    public @NotNull Class<ItemStack> getObjectType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("item_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull ItemStack item, @NotNull TagParser tags) {
        Tag.StringParser stackAmount = (args, context) -> item.getAmount() + "";
        return new StringTagContainer(tags)
            .add(Tag.string("base64", (args, ctx) -> Base64.getEncoder().encodeToString(item.serializeAsBytes())))
            .add(Tag.string("amount", stackAmount))
            .add(Tag.string("stack", stackAmount))
            .add(Tag.string("count", stackAmount))
            .add(Tag.string("max_stack_size", (args, context) -> CruxItem.getMaxStackSize(item) + ""))
            .add(Tag.string("max_durability", (args, context) -> CruxItem.getMaxDurability(item) + ""))
            .add(Tag.string("damage", (args, context) ->{
                if(!(item.getItemMeta() instanceof Damageable meta)) return "0";
                return meta.hasDamage() ? meta.getDamage() + "" : "0";
            }))
            .add(Tag.string("durability", (args, context) ->{
                if(!(item.getItemMeta() instanceof Damageable meta)) return "0";
                return (CruxItem.getMaxDurability(item) - meta.getDamage()) + "";
            }))
            .add(Tag.string("max_damage", (args, context) -> CruxItem.getMaxDurability(item) + ""))
            .add(Tag.string("type", (args, context) -> Crux.handlers().item().getType(item).asString()))
            .add(Tag.string("custom_model_data", (args, context) ->{
                ItemMeta meta = item.getItemMeta();
                if(meta==null) return "0";
                return (meta.hasCustomModelData() ? meta.getCustomModelData() : 0) + "";
            }))
            .add(Tag.string("as_string", (args, context) ->{
                ItemMeta meta = item.getItemMeta();
                if(meta==null) return null;
                return meta.getAsString();
            }))
            .add(Tag.string("enchant_level", (args, context) ->{
                ItemMeta meta = item.getItemMeta();
                if(meta==null) return "0";
                Key key = Key.key(args.get(0));
                Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);
                if(enchantment == null) return key + " enchant not found";
                return meta.getEnchantLevel(enchantment) + "";
            }))
            .add(Tag.string("rarity", (args, context) ->{
                ItemMeta meta = item.getItemMeta();
                if(meta==null) return null;
                return meta.getRarity().name().toLowerCase();
            }))
            .add(Tag.string("unbreakable", (args, context) ->{
                ItemMeta meta = item.getItemMeta();
                if(meta==null) return null;
                return meta.isUnbreakable() + "";
            }))
            .add(Tag.string("fire_resistant", (args, context) ->{
                ItemMeta meta = item.getItemMeta();
                if(meta==null) return null;
                return meta.isFireResistant() + "";
            }))
            .add(Tag.string("hide_tooltip", (args, context) ->{
                ItemMeta meta = item.getItemMeta();
                if(meta==null) return null;
                return meta.isHideTooltip() + "";
            }))
            .add(Tag.string("component", (args, context) ->{
                String id = args.get(0);
                switch (id.toString()){
                    case "color" ->{
                        DyedItemColor color = item.getData(DataComponentTypes.DYED_COLOR);
                        if(color != null) return CruxColor.colorToHex(color.color());
                        DyeColor base = item.getData(DataComponentTypes.BASE_COLOR);
                        if(base != null) return CruxColor.colorToHex(base.getColor());
                        MapItemColor map = item.getData(DataComponentTypes.MAP_COLOR);
                        if(map != null) return CruxColor.colorToHex(map.color());
                        return "none";
                    }
                }
                return null;
            }))
            .add(Tag.string("crux_component", (args, ctx) ->{
                Key key = Crux.key(ctx.deserializeString(args.get(0)));
                var type = CruxRegistries.DATA_COMPONENT_TYPE.get(key);
                if(type == null) return "datacomponenttype " + key + " not found";
                boolean defaultData = !args.has(2) || CruxString.parseBoolean(ctx.deserializeString(args.get(2)));

                Object got;
                if(defaultData) got = CruxItem.wrap(item).getOrDefaultData(type);
                else got = CruxItem.wrap(item).get(type);
                if(got == null) return args.has(1) ? ctx.deserializeString(args.get(1)) : "null";
                return got + "";
            }))
            ;
    }
}
