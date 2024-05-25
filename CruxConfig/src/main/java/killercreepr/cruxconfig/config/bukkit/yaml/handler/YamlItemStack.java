package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//todo enchants, attributes, color, food, item flags,
public class YamlItemStack implements YamlObjectHandler<ItemStack> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull ItemStack item) {
        YamlRegistry registry = context.getRegistry();
        YamlObject o = new YamlObject()
                .add("material", registry.serializeObject(item.getType()))
                .add("amount", registry.serializeObject(item.getAmount()))
                ;
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            Component name = meta.displayName();
            if(name != null) o.add("display_name", registry.serializeObject(name));
            List<Component> lore = meta.lore();
            if(lore != null) o.add("lore", registry.serializeObject(lore));

            if(meta.isUnbreakable()){
                o.add("unbreakable", registry.serializeObject(meta.isUnbreakable()));
            }
            if(meta.hasEnchantmentGlintOverride()){
                o.add("enchant_glint_override", registry.serializeObject(meta.getEnchantmentGlintOverride()));
            }

            if(meta.hasMaxStackSize()){
                o.add("max_stack_size", registry.serializeObject(meta.getMaxStackSize()));
            }
            if(meta.hasCustomModelData()){
                o.add("custom_model_data", registry.serializeObject(meta.getCustomModelData()));
            }
            if(meta.hasRarity()){
                o.add("rarity", registry.serializeObject(meta.getRarity()));
            }
            if(meta.isFireResistant()){
                o.add("fire_resistant", registry.serializeObject(meta.isFireResistant()));
            }
            if(meta.isHideTooltip()){
                o.add("hide_tooltip", registry.serializeObject(meta.isHideTooltip()));
            }
            if(meta instanceof ArmorMeta m){
                if(m.hasTrim()){
                    o.add("armor_trim", registry.serializeObject(m.getTrim()));
                }
            }
        }
        return o;
    }

    @Override
    public @Nullable ItemStack deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e){
        return deserializeFromYaml(context, e, null);
    }

    public @Nullable ItemStack buildItem(@NotNull YamlContext context, @Nullable YamlElement e){
        YamlRegistry registry = context.getRegistry();
        if(e instanceof YamlPrimitive s){
            Material material = registry.deserialize(Material.class, s, context);
            if(material == null) return null;
            return new ItemStack(material);
        }
        if(!(e instanceof YamlObject o)) return null;
        Material material = registry.deserialize(Material.class, o.get("material"));
        if(material == null) return null;
        return new ItemStack(material);
    }

    public @Nullable ItemStack deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable ItemStack stack) {
        if(stack == null) stack = buildItem(context, e);
        if(stack == null) return null;

        YamlRegistry registry = context.getRegistry();
        if(e instanceof YamlPrimitive s){
            Material material = registry.deserialize(Material.class, s, context);
            if(material != null) stack = stack.withType(material);
        }
        if(!(e instanceof YamlObject o)) return stack;
        Material material = registry.deserialize(Material.class, o.get("material"));
        if(material != null) stack = stack.withType(material);
        int amount = o.getObject(Number.class, "amount", 1).intValue();
        CruxItem item = new CruxItem(stack).amount(amount);

        String displayName = o.getObject(String.class, "display_name");
        List<String> lore = registry.deserialize((Class<List<String>>) (Class<?>) List.class, o.get("lore"));

        if(displayName != null) item.displayName(displayName);
        if(lore != null) item.loreFromString(lore);
        Boolean unbreakable = o.getObject(Boolean.class, "unbreakable");
        if(unbreakable != null) item.unbreakable(unbreakable);

        item.editMeta(meta ->{
            Boolean glint = o.searchForObject(Boolean.class, "enchant_glint_override",
                    "glint_override");
            if(glint != null) meta.setEnchantmentGlintOverride(glint);

            Integer maxStack = o.getObject(Integer.class, "max_stack_size");
            if(maxStack != null) meta.setMaxStackSize(maxStack);

            Integer cmd = o.searchForObject(Integer.class, "custom_model_data", "model_data", "cmd");
            if(cmd != null) meta.setCustomModelData(cmd);

            ItemRarity rarity = registry.deserialize(ItemRarity.class, o.get("rarity"));
            if(rarity != null) meta.setRarity(rarity);

            Boolean value = o.getObject(Boolean.class, "fire_resistant");
            if(value != null) meta.setFireResistant(value);

            value = o.getObject(Boolean.class, "hide_tooltip");
            if(value != null) meta.setHideTooltip(value);
        });

        item.editMeta(ArmorMeta.class, meta ->{
            ArmorTrim trim = registry.deserialize(ArmorTrim.class, o.search("armor_trim", "trim"));
            if(trim != null) meta.setTrim(trim);
        });
        return item.item();
    }
}
