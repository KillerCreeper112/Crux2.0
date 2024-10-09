package killercreepr.crux.tags.standard.object;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.Crux;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.crux.util.CruxItem;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            .add(Tag.string("amount", stackAmount))
            .add(Tag.string("stack", stackAmount))
            .add(Tag.string("count", stackAmount))
            .add(Tag.string("max_stack_size", (args, context) -> CruxItem.getMaxStackSize(item) + ""))
            .add(Tag.string("max_durability", (args, context) -> CruxItem.getMaxDurability(item) + ""))
            .add(Tag.string("damage", (args, context) ->{
                if(!(item.getItemMeta() instanceof Damageable meta)) return "0";
                return meta.getDamage() + "";
            }))
            .add(Tag.string("durability", (args, context) ->{
                if(!(item.getItemMeta() instanceof Damageable meta)) return "0";
                return (CruxItem.getMaxDurability(item) - meta.getDamage()) + "";
            }))
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
            ;
    }
}
