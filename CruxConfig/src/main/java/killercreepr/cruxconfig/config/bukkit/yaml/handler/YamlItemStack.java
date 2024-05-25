package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.element.YamlPrimitive;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YamlItemStack implements YamlObjectHandler<ItemStack> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull ItemStack item) {
        YamlRegistry registry = context.getRegistry();
        return new YamlObject()
                .add("material", registry.serializeObject(item.getType()))
                .add("amount", registry.serializeObject(item.getAmount()))
                .add("display_name", registry.serializeObject())
                ;
    }

    @Override
    public @Nullable ItemStack deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        YamlRegistry registry = context.getRegistry();
        if(e instanceof YamlPrimitive s){
            Material material = registry.deserialize(Material.class, s, context);
            if(material == null) return null;
            return new ItemStack(material);
        }
        if(!(e instanceof YamlObject o)) return null;
        Material material = registry.deserialize(Material.class, o.get("material"));
        if(material == null) return null;
        int amount = o.getObject(Number.class, "amount", 1).intValue();
        CruxItem item = new CruxItem(new ItemStack(material, amount));

        String displayName = o.getObject(String.class, "display_name");
        List<String> lore = o.getObject(List.class, "lore");

        item.displayName(displayName);
        item.loreFromString(lore);
        item.unbreakable(o.getObject(Boolean.class, "unbreakable", false));
        item.editMeta(meta ->{
            meta.setEnchantmentGlintOverride(o.searchForObject(Boolean.class, "enchant_glint_override",
                    "glint_override"));
            meta.setMaxStackSize(o.getObject(Integer.class, "max_stack_size"));
            meta.setCustomModelData(o.searchForObject(Integer.class, "custom_model_data", "model_data", "cmd"));
            meta.setRarity(registry.deserialize(ItemRarity.class, o.get("rarity")));

            Boolean value = o.getObject(Boolean.class, "fire_resistant");
            if(value != null) meta.setFireResistant(value);

            value = o.getObject(Boolean.class, "hide_tooltip");
            if(value != null) meta.setHideTooltip(value);
        });

        item.editMeta(ArmorMeta.class, meta ->{
            ArmorTrim trim = registry.deserialize(ArmorTrim.class, o.search("armor_trim", "trim"));
            meta.setTrim(trim);
        });
        return item.item();
    }
}
