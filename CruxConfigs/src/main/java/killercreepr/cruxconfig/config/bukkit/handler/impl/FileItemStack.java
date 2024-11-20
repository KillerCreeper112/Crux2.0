package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.util.CruxItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
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
@JsonSerializer(id = "itemstack")
public class FileItemStack extends SimpleFileHandler<ItemStack> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemStack item) {
        FileRegistry registry = context.getRegistry();
        FileObject o = new FileObject()
                .add("material", registry.serializeToFile(item.getType()))
                .add("amount", registry.serializeToFile(item.getAmount()))
                ;
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            Component name = meta.displayName();
            if(name != null) o.add("display_name", registry.serializeToFile(name));
            List<Component> lore = meta.lore();
            if(lore != null) o.add("lore", registry.serializeToFile(lore));

            if(meta.isUnbreakable()){
                o.add("unbreakable", registry.serializeToFile(meta.isUnbreakable()));
            }
            if(meta.hasEnchantmentGlintOverride()){
                o.add("enchant_glint_override", registry.serializeToFile(meta.getEnchantmentGlintOverride()));
            }

            if(meta.hasMaxStackSize()){
                o.add("max_stack_size", registry.serializeToFile(meta.getMaxStackSize()));
            }
            if(meta.hasCustomModelData()){
                o.add("custom_model_data", registry.serializeToFile(meta.getCustomModelData()));
            }
            if(meta.hasRarity()){
                o.add("rarity", registry.serializeToFile(meta.getRarity()));
            }
            if(meta.isFireResistant()){
                o.add("fire_resistant", registry.serializeToFile(meta.isFireResistant()));
            }
            if(meta.isHideTooltip()){
                o.add("hide_tooltip", registry.serializeToFile(meta.isHideTooltip()));
            }
            if(meta instanceof ArmorMeta m){
                if(m.hasTrim()){
                    o.add("armor_trim", registry.serializeToFile(m.getTrim()));
                }
            }
        }
        return o;
    }

    @Override
    public @Nullable ItemStack deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return deserializeFromFile(context, e, null);
    }

    /*@Override
    public @Nullable ItemStack deserializeFromYaml(@NotNull FileContext<?> context, @Nullable YamlElement e){
        return deserializeFromYaml(context, e, null);
    }*/

    public @Nullable ItemStack buildItem(@NotNull FileContext<?> context, @Nullable FileElement e){
        FileRegistry registry = context.getRegistry();
        if(e instanceof FilePrimitive s){
            Material material = registry.deserializeFromFile(Material.class, s, context);
            if(material == null) return null;
            return new ItemStack(material);
        }
        if(!(e instanceof FileObject o)) return null;
        Material material = registry.deserializeFromFile(Material.class, o.get("material"));
        if(material == null) return null;
        return new ItemStack(material);
    }

    public @Nullable ItemStack deserializeFromFile(@NotNull FileContext<?> context, @Nullable FileElement e, @Nullable ItemStack stack) {
        if(stack == null) stack = buildItem(context, e);
        if(stack == null) return null;

        FileRegistry registry = context.getRegistry();
        if(e instanceof FilePrimitive s){
            Material material = registry.deserializeFromFile(Material.class, s, context);
            if(material != null) stack = stack.withType(material);
        }
        if(!(e instanceof FileObject o)) return stack;
        Material material = registry.deserializeFromFile(Material.class, o.get("material"));
        if(material != null) stack = stack.withType(material);
        int amount = o.getObject(Number.class, "amount", 1).intValue();
        CruxItem item = new CruxItem(stack).amount(amount);

        String customName = o.getObject(String.class, "custom_name");
        String itemName = o.getObject(String.class, "item_name");
        List<String> lore = registry.deserializeFromFile((Class<List<String>>) (Class<?>) List.class, o.get("lore"));

        if(customName != null) item.customName(customName);
        if(itemName != null) item.itemName(customName);
        if(lore != null) item.loreFromString(lore);
        Boolean unbreakable = o.getObject(Boolean.class, "unbreakable");
        if(unbreakable != null) item.unbreakable(unbreakable);

        item.editMeta(meta ->{
            Boolean glint = o.searchForObject(Boolean.class, "enchant_glint_override",
                    "glint_override", "glow");
            if(glint != null) meta.setEnchantmentGlintOverride(glint);

            Integer maxStack = o.getObject(Integer.class, "max_stack_size");
            if(maxStack != null) meta.setMaxStackSize(maxStack);

            Integer cmd = o.searchForObject(Integer.class, "custom_model_data", "model_data", "cmd");
            if(cmd != null) meta.setCustomModelData(cmd);

            ItemRarity rarity = registry.deserializeFromFile(ItemRarity.class, o.get("rarity"));
            if(rarity != null) meta.setRarity(rarity);

            Boolean value = o.getObject(Boolean.class, "fire_resistant");
            if(value != null) meta.setFireResistant(value);

            value = o.getObject(Boolean.class, "hide_tooltip");
            if(value != null) meta.setHideTooltip(value);
        });

        item.editMeta(ArmorMeta.class, meta ->{
            ArmorTrim trim = registry.deserializeFromFile(ArmorTrim.class, o.search("armor_trim", "trim"));
            if(trim != null) meta.setTrim(trim);
        });
        return item.item();
    }
    @Override
    public @NotNull String jsonSerializerID() {
        return "itemstack";
    }
}
