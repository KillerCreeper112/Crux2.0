package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.item.DynamicItem;
import killercreepr.crux.item.DynamicItemComponent;
import killercreepr.crux.item.TestItem;
import killercreepr.crux.item.components.DynamicSingleValueComponent;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
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
import java.util.Map;

//todo enchants, attributes, color, food, item flags,
public class YamlDynamicItem implements YamlObjectHandler<DynamicItem> {
    protected final MappedRegistry<String, YamlDynamicItemComponent<?>> COMPONENT_REGISTRY = new SimpleMappedRegistry<>();

    public MappedRegistry<String, YamlDynamicItemComponent<?>> getComponentRegistry() {
        return COMPONENT_REGISTRY;
    }

    public YamlDynamicItem() {
        COMPONENT_REGISTRY.register("name", new YamlDynamicItemComponent<DynamicSingleValueComponent>() {
            @Override
            public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull DynamicSingleValueComponent object) {
                return new YamlGeneric(object.getValue());
            }

            @Override
            public @Nullable DynamicSingleValueComponent deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
                return null;
            }
        });
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull DynamicItem item) {
        return null;//todo
    }

    @Override
    public @Nullable DynamicItem deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e){
        return deserializeFromYaml(context, e, null);
    }

    public @Nullable DynamicItem buildItem(@NotNull YamlContext context, @Nullable YamlElement e){
        YamlRegistry registry = context.getRegistry();
        if(e instanceof YamlGeneric s){
            return new TestItem.Builder(s.getAsString()).build();
        }
        if(!(e instanceof YamlObject o)) return null;
        if(o.get("material") instanceof YamlGeneric s){
            return new TestItem.Builder(s.getAsString()).build();
        }
        return null;
    }

    public @Nullable DynamicItem deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable DynamicItem stack) {
        if(stack == null) stack = buildItem(context, e);
        if(stack == null) return null;

        YamlRegistry registry = context.getRegistry();
        if(e instanceof YamlGeneric s){
            return stack.withType(s.getAsString());
        }
        if(!(e instanceof YamlObject o)) return stack;
        if(o.get("material") instanceof YamlGeneric s){
            stack = stack.withType(s.getAsString());
        }
        if(o.get("amount") instanceof YamlGeneric s){
            stack = stack.withAmount(s.getAsString());
        }

        for(Map.Entry<String, YamlElement> entry : o){
            YamlDynamicItemComponent<?> handler = COMPONENT_REGISTRY.get(entry.getKey());
            if(handler == null) continue;
            DynamicItemComponent component = handler.deserializeFromYaml(context, entry.getValue());
            if(component == null) continue;
            stack = stack.withComponent(component);
        }

        String displayName = o.getObject(String.class, "display_name");
        List<String> lore = registry.deserialize((Class<List<String>>) (Class<?>) List.class, o.get("lore"));

        if(displayName != null) item.displayName(displayName);
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
