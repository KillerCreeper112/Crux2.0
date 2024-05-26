package killercreepr.cruxconfig.config.bukkit.yaml.handler.item;

import killercreepr.crux.item.BukkitDynamicItem;
import killercreepr.crux.item.DynamicItem;
import killercreepr.crux.item.DynamicItemComponent;
import killercreepr.crux.item.components.*;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.yaml.handler.item.component.YamlDynamicItemComponent;
import killercreepr.cruxconfig.config.bukkit.yaml.handler.item.component.YamlGenericSingleDynamicComponent;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

//todo enchants, attributes, color, food, item flags,
public class YamlDynamicItem implements YamlObjectHandler<DynamicItem> {
    protected final MappedRegistry<String, YamlDynamicItemComponent<?>> COMPONENT_REGISTRY = new SimpleMappedRegistry<>();

    public MappedRegistry<String, YamlDynamicItemComponent<?>> getComponentRegistry() {
        return COMPONENT_REGISTRY;
    }

    public YamlDynamicItem() {
        COMPONENT_REGISTRY.register("name", new YamlGenericSingleDynamicComponent<>(DynamicItemName.class) {
            @Override
            public @NotNull DynamicItemName deserialize(@NotNull Object object) {
                return new DynamicItemName(object);
            }
        });

        COMPONENT_REGISTRY.register("lore", new YamlGenericSingleDynamicComponent<>(DynamicItemLore.class) {
            @Override
            public @NotNull DynamicItemLore deserialize(@NotNull Object object) {
                return new DynamicItemLore(object);
            }
        });

        COMPONENT_REGISTRY.register("custom_model_data", new YamlGenericSingleDynamicComponent<>(DynamicItemCustomModelData.class) {
            @Override
            public @NotNull DynamicItemCustomModelData deserialize(@NotNull Object object) {
                return new DynamicItemCustomModelData(object);
            }
        });

        COMPONENT_REGISTRY.register("unbreakable", new YamlGenericSingleDynamicComponent<>(DynamicItemUnbreakable.class) {
            @Override
            public @NotNull DynamicItemUnbreakable deserialize(@NotNull Object object) {
                return new DynamicItemUnbreakable(object);
            }
        });

        COMPONENT_REGISTRY.register("enchant_glint_override", new YamlGenericSingleDynamicComponent<>(DynamicItemEnchantGlintOverride.class) {
            @Override
            public @NotNull DynamicItemEnchantGlintOverride deserialize(@NotNull Object object) {
                return new DynamicItemEnchantGlintOverride(object);
            }
        });

        COMPONENT_REGISTRY.register("max_stack_size", new YamlGenericSingleDynamicComponent<>(DynamicItemMaxStackSize.class) {
            @Override
            public @NotNull DynamicItemMaxStackSize deserialize(@NotNull Object object) {
                return new DynamicItemMaxStackSize(object);
            }
        });

        COMPONENT_REGISTRY.register("fire_resistant", new YamlGenericSingleDynamicComponent<>(DynamicItemFireResistant.class) {
            @Override
            public @NotNull DynamicItemFireResistant deserialize(@NotNull Object object) {
                return new DynamicItemFireResistant(object);
            }
        });

        COMPONENT_REGISTRY.register("armor_trim", new YamlDynamicItemComponent<>(DynamicItemArmorTrim.class) {

            @Override
            public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull DynamicItemArmorTrim object) {
                YamlRegistry registry = context.getRegistry();
                return new YamlObject()
                        .add("material", registry.serializeObject(object.getTrimMaterial()))
                        .add("pattern", registry.serializeObject(object.getTrimPattern()))
                        ;
            }

            @Override
            public @Nullable DynamicItemArmorTrim deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
                if(!(e instanceof YamlObject o)) return null;
                YamlRegistry registry = context.getRegistry();
                Object material = registry.deserialize(String.class, o.get("material"));
                if(material==null) return null;
                Object pattern = registry.deserialize(String.class, o.get("pattern"));
                if(pattern==null) return null;
                return new DynamicItemArmorTrim(material, pattern);
            }
        });

        COMPONENT_REGISTRY.register("hide_tooltip", new YamlGenericSingleDynamicComponent<>(DynamicItemHideTooltip.class) {
            @Override
            public @NotNull DynamicItemHideTooltip deserialize(@NotNull Object object) {
                return new DynamicItemHideTooltip(object);
            }
        });

        COMPONENT_REGISTRY.register("rarity", new YamlGenericSingleDynamicComponent<>(DynamicItemRarity.class) {
            @Override
            public @NotNull DynamicItemRarity deserialize(@NotNull Object object) {
                return new DynamicItemRarity(object);
            }
        });
    }

    public YamlDynamicItem registerComponents(@NotNull YamlRegistry registry){
        for(YamlDynamicItemComponent<?> c : COMPONENT_REGISTRY){
            registry.registerHandler(c.getType(), c);
        }
        return this;
    }

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull DynamicItem item) {
        YamlRegistry registry = context.getRegistry();
        YamlObject o = new YamlObject();
        o.add("material", registry.serializeObject(item.material()));
        o.add("amount", registry.serializeObject(item.amount()));
        if(item.components() != null){
            item.components().forEach((key, value) ->{
                o.add(key, registry.serializeObject(value));
            });
        }
        return o;
    }

    @Override
    public @Nullable DynamicItem deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e){
        return deserializeFromYaml(context, e, null);
    }

    public @Nullable DynamicItem buildItem(@NotNull YamlContext context, @Nullable YamlElement e){
        if(e instanceof YamlGeneric s){
            return new BukkitDynamicItem.Builder(s.getAsString()).build();
        }
        if(!(e instanceof YamlObject o)) return null;
        if(o.get("material") instanceof YamlGeneric s){
            return new BukkitDynamicItem.Builder(s.getAsString()).build();
        }
        return null;
    }

    public @Nullable DynamicItem deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable DynamicItem stack) {
        if(stack == null) stack = buildItem(context, e);
        if(stack == null) return null;

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
        return stack;

        /*item.editMeta(meta ->{
            ItemRarity rarity = registry.deserialize(ItemRarity.class, o.get("rarity"));
            if(rarity != null) meta.setRarity(rarity);
        });
        return item.item();*/
    }
}
