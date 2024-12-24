package killercreepr.cruxconfig.config.bukkit.handler.impl.item;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.item.dynamic.component.persistence.TypedDynamicPersistentTag;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.item.dynamic.BukkitDynamicItem;
import killercreepr.crux.core.item.dynamic.component.*;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.FileDynamicItemComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.FileGenericSingleDynamicComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//todo food,
@JsonSerializer(id = "dynamic_item")
public class FileDynamicItem extends SimpleFileHandler<DynamicItem> {
    protected final MappedRegistry<String, FileDynamicItemComponent<?>> COMPONENT_REGISTRY = new SimpleMappedRegistry<>();

    public MappedRegistry<String, FileDynamicItemComponent<?>> getComponentRegistry() {
        return COMPONENT_REGISTRY;
    }

    public FileDynamicItem() {
        COMPONENT_REGISTRY.register("name", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemName.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_name";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemName deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemName(object);
            }
        });

        COMPONENT_REGISTRY.register("lore", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemLore.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_lore";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemLore deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemLore(object);
            }
        });

        COMPONENT_REGISTRY.register("custom_model_data", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemCustomModelData.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_custom_model_data";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemCustomModelData deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemCustomModelData(object);
            }
        });

        COMPONENT_REGISTRY.register("unbreakable", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemUnbreakable.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_unbreakable";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemUnbreakable deserialize(@NotNull Object object) {
                return new DynamicItemUnbreakable(object);
            }
        });

        COMPONENT_REGISTRY.register("enchant_glint_override", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemEnchantGlintOverride.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_enchant_glint_override";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemEnchantGlintOverride deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemEnchantGlintOverride(object);
            }
        });

        COMPONENT_REGISTRY.register("max_stack_size", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemMaxStackSize.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_max_stack_size";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemMaxStackSize deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemMaxStackSize(object);
            }
        });

        COMPONENT_REGISTRY.register("max_damage", new FileGenericSingleDynamicComponent<>(DynamicItemMaxDamage.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_max_damage";
            }

            @Override
            public @NotNull DynamicItemMaxDamage deserialize(@NotNull Object object) {
                return new DynamicItemMaxDamage(object);
            }
        });

        COMPONENT_REGISTRY.register("damage", new FileGenericSingleDynamicComponent<>(DynamicItemDamage.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_damage";
            }

            @Override
            public @NotNull DynamicItemDamage deserialize(@NotNull Object object) {
                return new DynamicItemDamage(object);
            }
        });

        COMPONENT_REGISTRY.register("fire_resistant", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemFireResistant.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_fire_resistant";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemFireResistant deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemFireResistant(object);
            }
        });

        COMPONENT_REGISTRY.register("armor_trim", new FileDynamicItemComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemArmorTrim.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_armor_trim";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemArmorTrim object) {
                FileRegistry registry = context.getRegistry();
                return new FileObject()
                        .add("material", registry.serializeToFile(object.getTrimMaterial()))
                        .add("pattern", registry.serializeToFile(object.getTrimPattern()))
                        ;
            }

            @Override
            public @Nullable killercreepr.crux.core.item.dynamic.component.DynamicItemArmorTrim deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                FileRegistry registry = context.getRegistry();
                Object material = registry.deserializeFromFile(String.class, o.get("material"));
                if(material==null) return null;
                Object pattern = registry.deserializeFromFile(String.class, o.get("pattern"));
                if(pattern==null) return null;
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemArmorTrim(material, pattern);
            }
        });

        COMPONENT_REGISTRY.register("hide_tooltip", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemHideTooltip.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_hide_tooltip";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemHideTooltip deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemHideTooltip(object);
            }
        });

        COMPONENT_REGISTRY.register("rarity", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemRarity.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_rarity";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemRarity deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemRarity(object);
            }
        });

        COMPONENT_REGISTRY.register("enchants", new FileDynamicItemComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemEnchants.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_enchants";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemEnchants object) {
                FileRegistry registry = context.getRegistry();
                FileObject o = new FileObject();
                object.getEnchants().forEach((key, value) -> o.add(key.toString(), registry.serializeToFile(value)));
                return o;
            }

            @Override
            public @Nullable killercreepr.crux.core.item.dynamic.component.DynamicItemEnchants deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                FileRegistry registry = context.getRegistry();
                Map<Object, Object> enchants = new HashMap<>();
                o.forEach((key, value) ->{
                    Object level = registry.deserializeFromFile(String.class, value);
                    if(level==null) return;
                    enchants.put(key, level);
                });
                if(enchants.isEmpty()) return null;
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemEnchants(enchants);
            }
        });

        COMPONENT_REGISTRY.register("color", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemColor.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_color";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemColor deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemColor(object);
            }
        });

        COMPONENT_REGISTRY.register("flags", new FileDynamicItemComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemFlags.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_flags";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemFlags object) {
                FileRegistry registry = context.getRegistry();
                FileArray o = new FileArray();
                object.getFlags().forEach(registry::serializeToFile);
                return o;
            }

            @Override
            public @Nullable killercreepr.crux.core.item.dynamic.component.DynamicItemFlags deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileArray o)) return null;
                FileRegistry registry = context.getRegistry();
                Collection<Object> flags = new HashSet<>();
                o.forEach((value) ->{
                    Object flag = registry.deserializeFromFile(String.class, value);
                    if(flag==null) return;
                    flags.add(flag);
                });
                if(flags.isEmpty()) return null;
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemFlags(flags);
            }
        });

        COMPONENT_REGISTRY.register("hide_flags", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemHideAllFlags.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_hide_flags";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemHideAllFlags deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemHideAllFlags(object);
            }
        });

        COMPONENT_REGISTRY.register("head", new FileGenericSingleDynamicComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemHead.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_head";
            }

            @Override
            public @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemHead deserialize(@NotNull Object object) {
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemHead(object);
            }
        });

        COMPONENT_REGISTRY.register("persistent_tags", new FileDynamicItemComponent<>(killercreepr.crux.core.item.dynamic.component.DynamicItemPersistentTags.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_persistent_tags";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull killercreepr.crux.core.item.dynamic.component.DynamicItemPersistentTags object) {
                FileRegistry registry = context.getRegistry();
                return registry.serializeToFile(object.getTags());
            }

            @Override
            public @Nullable killercreepr.crux.core.item.dynamic.component.DynamicItemPersistentTags deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                Collection<TypedDynamicPersistentTag> tags = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<TypedDynamicPersistentTag>>(){}.getType(), e
                );
                if(tags == null || tags.isEmpty()) return null;
                return new killercreepr.crux.core.item.dynamic.component.DynamicItemPersistentTags(tags);
            }
        });

        COMPONENT_REGISTRY.register("crux_components", new FileGenericSingleDynamicComponent<>(DynamicItemCruxComponents.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_crux_components";
            }

            @Override
            public @NotNull DynamicItemCruxComponents deserialize(@NotNull Object object) {
                return new DynamicItemCruxComponents(object);
            }
        });

        COMPONENT_REGISTRY.register("attributes", new FileDynamicItemComponent<>(DynamicItemAttributes.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_attributes";
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DynamicItemAttributes object) {
                FileRegistry registry = context.getRegistry();
                FileObject o = new FileObject();
                object.getAttributes().forEach((key, value) -> o.add(key.toString(), registry.serializeToFile(value)));
                return o;
            }

            @Override
            public @Nullable DynamicItemAttributes deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileArray o)) return null;
                FileRegistry registry = context.getRegistry();
                Map<Object, Collection<DynamicAttributeModifier>> attributes = new HashMap<>();
                o.forEach(element ->{
                    if(!(element instanceof FileObject oo)) return;
                    String key = oo.getObject(String.class, "attribute");
                    if(key == null) return;
                    Collection<DynamicAttributeModifier> level = registry.deserializeFromFile(
                        new TypeToken<Set<DynamicAttributeModifier>>(){}.getType(), oo.get("values"));
                    if(level==null) return;
                    attributes.put(key, level);
                });
                if(attributes.isEmpty()) return null;
                return new DynamicItemAttributes(attributes);
            }
        });
    }

    public FileDynamicItem registerComponents(@NotNull FileRegistry registry){
        for(FileDynamicItemComponent<?> c : COMPONENT_REGISTRY){
            registry.registerFileHandler(c.getType(), c);
        }
        return this;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DynamicItem item) {
        FileRegistry registry = context.getRegistry();
        FileObject o = new FileObject();
        o.add("material", registry.serializeToFile(item.material()));
        o.add("amount", registry.serializeToFile(item.amount()));
        Map<String, DynamicItemComponent> components = item.components();
        if(components != null){
            components.forEach((key, value) ->{
                o.add(key, registry.serializeToFile(value));
            });
        }
        return o;
    }

    @Override
    public @Nullable DynamicItem deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return deserializeFromYaml(context, e, null);
    }

    public @Nullable DynamicItem buildItem(@NotNull FileContext<?> context, @NotNull FileElement e){
        if(e instanceof FileGeneric s){
            return new BukkitDynamicItem.Builder(s.getAsString()).build();
        }
        if(!(e instanceof FileObject o)) return null;
        if(o.get("material") instanceof FileGeneric s){
            return new BukkitDynamicItem.Builder(s.getAsString()).build();
        }
        return o.isEmpty() ? null : new BukkitDynamicItem.Builder("").build();
    }

    public @Nullable DynamicItem deserializeFromYaml(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable DynamicItem stack) {
        if(stack == null) stack = buildItem(context, e);
        if(stack == null) return null;

        if(e instanceof FileGeneric s){
            return stack.withType(s.getAsString());
        }
        if(!(e instanceof FileObject o)) return stack;
        if(o.get("material") instanceof FileGeneric s){
            stack = stack.withType(s.getAsString());
        }
        if(o.get("amount") instanceof FileGeneric s){
            stack = stack.withAmount(s.getAsString());
        }

        for(Map.Entry<String, FileElement> entry : o){
            FileDynamicItemComponent<?> handler = COMPONENT_REGISTRY.get(entry.getKey());
            if(handler == null) continue;
            DynamicItemComponent component = handler.deserializeFromFile(context, entry.getValue());
            if(component == null) continue;
            stack = stack.withComponent(component);
        }

        if(o.get("merge") instanceof FileObject oo){
            for(Map.Entry<String, FileElement> entry : oo){
                FileDynamicItemComponent<?> handler = COMPONENT_REGISTRY.get(entry.getKey());
                if(handler == null) continue;
                DynamicItemComponent component = handler.deserializeFromFile(context, entry.getValue());
                if(component == null) continue;
                stack = stack.mergeComponent(component);
            }
        }

        return stack;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "dynamic_item";
    }
}
