package killercreepr.cruxconfig.config.bukkit.handler.impl.item;

import killercreepr.crux.item.dynamic.BukkitDynamicItem;
import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.item.dynamic.DynamicItemComponent;
import killercreepr.crux.item.dynamic.components.*;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.FileDynamicItemComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.FileDynamicPersistentTag;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

//todo attributes, food,
@JsonSerializer(id = "dynamic_item")
public class FileDynamicItem extends SimpleFileHandler<DynamicItem> {
    protected final MappedRegistry<String, FileDynamicItemComponent<?>> COMPONENT_REGISTRY = new SimpleMappedRegistry<>();

    public MappedRegistry<String, FileDynamicItemComponent<?>> getComponentRegistry() {
        return COMPONENT_REGISTRY;
    }

    public FileDynamicItem() {
        COMPONENT_REGISTRY.register("name", new FileGenericSingleDynamicComponent<>(DynamicItemName.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_name";
            }

            @Override
            public @NotNull DynamicItemName deserialize(@NotNull Object object) {
                return new DynamicItemName(object);
            }
        });

        COMPONENT_REGISTRY.register("lore", new FileGenericSingleDynamicComponent<>(DynamicItemLore.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_lore";
            }

            @Override
            public @NotNull DynamicItemLore deserialize(@NotNull Object object) {
                return new DynamicItemLore(object);
            }
        });

        COMPONENT_REGISTRY.register("custom_model_data", new FileGenericSingleDynamicComponent<>(DynamicItemCustomModelData.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_custom_model_data";
            }

            @Override
            public @NotNull DynamicItemCustomModelData deserialize(@NotNull Object object) {
                return new DynamicItemCustomModelData(object);
            }
        });

        COMPONENT_REGISTRY.register("unbreakable", new FileGenericSingleDynamicComponent<>(DynamicItemUnbreakable.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_unbreakable";
            }

            @Override
            public @NotNull DynamicItemUnbreakable deserialize(@NotNull Object object) {
                return new DynamicItemUnbreakable(object);
            }
        });

        COMPONENT_REGISTRY.register("enchant_glint_override", new FileGenericSingleDynamicComponent<>(DynamicItemEnchantGlintOverride.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_enchant_glint_override";
            }

            @Override
            public @NotNull DynamicItemEnchantGlintOverride deserialize(@NotNull Object object) {
                return new DynamicItemEnchantGlintOverride(object);
            }
        });

        COMPONENT_REGISTRY.register("max_stack_size", new FileGenericSingleDynamicComponent<>(DynamicItemMaxStackSize.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_max_stack_size";
            }

            @Override
            public @NotNull DynamicItemMaxStackSize deserialize(@NotNull Object object) {
                return new DynamicItemMaxStackSize(object);
            }
        });

        COMPONENT_REGISTRY.register("fire_resistant", new FileGenericSingleDynamicComponent<>(DynamicItemFireResistant.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_fire_resistant";
            }

            @Override
            public @NotNull DynamicItemFireResistant deserialize(@NotNull Object object) {
                return new DynamicItemFireResistant(object);
            }
        });

        COMPONENT_REGISTRY.register("armor_trim", new FileDynamicItemComponent<>(DynamicItemArmorTrim.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_armor_trim";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DynamicItemArmorTrim object) {
                FileRegistry registry = context.getRegistry();
                return new FileObject()
                        .add("material", registry.serializeToFile(object.getTrimMaterial()))
                        .add("pattern", registry.serializeToFile(object.getTrimPattern()))
                        ;
            }

            @Override
            public @Nullable DynamicItemArmorTrim deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                FileRegistry registry = context.getRegistry();
                Object material = registry.deserializeFromFile(String.class, o.get("material"));
                if(material==null) return null;
                Object pattern = registry.deserializeFromFile(String.class, o.get("pattern"));
                if(pattern==null) return null;
                return new DynamicItemArmorTrim(material, pattern);
            }
        });

        COMPONENT_REGISTRY.register("hide_tooltip", new FileGenericSingleDynamicComponent<>(DynamicItemHideTooltip.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_hide_tooltip";
            }

            @Override
            public @NotNull DynamicItemHideTooltip deserialize(@NotNull Object object) {
                return new DynamicItemHideTooltip(object);
            }
        });

        COMPONENT_REGISTRY.register("rarity", new FileGenericSingleDynamicComponent<>(DynamicItemRarity.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_rarity";
            }

            @Override
            public @NotNull DynamicItemRarity deserialize(@NotNull Object object) {
                return new DynamicItemRarity(object);
            }
        });

        COMPONENT_REGISTRY.register("enchants", new FileDynamicItemComponent<>(DynamicItemEnchants.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_enchants";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DynamicItemEnchants object) {
                FileRegistry registry = context.getRegistry();
                FileObject o = new FileObject();
                object.getEnchants().forEach((key, value) -> o.add(key.toString(), registry.serializeToFile(value)));
                return o;
            }

            @Override
            public @Nullable DynamicItemEnchants deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                FileRegistry registry = context.getRegistry();
                Map<Object, Object> enchants = new HashMap<>();
                o.forEach((key, value) ->{
                    Object level = registry.deserializeFromFile(String.class, value);
                    if(level==null) return;
                    enchants.put(key, level);
                });
                if(enchants.isEmpty()) return null;
                return new DynamicItemEnchants(enchants);
            }
        });

        COMPONENT_REGISTRY.register("color", new FileGenericSingleDynamicComponent<>(DynamicItemColor.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_color";
            }

            @Override
            public @NotNull DynamicItemColor deserialize(@NotNull Object object) {
                return new DynamicItemColor(object);
            }
        });

        COMPONENT_REGISTRY.register("flags", new FileDynamicItemComponent<>(DynamicItemFlags.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_flags";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DynamicItemFlags object) {
                FileRegistry registry = context.getRegistry();
                FileArray o = new FileArray();
                object.getFlags().forEach(registry::serializeToFile);
                return o;
            }

            @Override
            public @Nullable DynamicItemFlags deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileArray o)) return null;
                FileRegistry registry = context.getRegistry();
                Collection<Object> flags = new HashSet<>();
                o.forEach((value) ->{
                    Object flag = registry.deserializeFromFile(String.class, value);
                    if(flag==null) return;
                    flags.add(flag);
                });
                if(flags.isEmpty()) return null;
                return new DynamicItemFlags(flags);
            }
        });

        COMPONENT_REGISTRY.register("hide_flags", new FileGenericSingleDynamicComponent<>(DynamicItemHideAllFlags.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_hide_flags";
            }

            @Override
            public @NotNull DynamicItemHideAllFlags deserialize(@NotNull Object object) {
                return new DynamicItemHideAllFlags(object);
            }
        });

        COMPONENT_REGISTRY.register("head", new FileGenericSingleDynamicComponent<>(DynamicItemHead.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_head";
            }

            @Override
            public @NotNull DynamicItemHead deserialize(@NotNull Object object) {
                return new DynamicItemHead(object);
            }
        });

        COMPONENT_REGISTRY.register("persistent_tags", new FileDynamicItemComponent<>(DynamicItemPersistentTags.class) {
            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_item_persistent_tags";
            }


            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DynamicItemPersistentTags object) {
                FileRegistry registry = context.getRegistry();
                FileObject o = new FileObject();
                object.getTags().forEach((key, value) -> o.add(key.toString(), registry.serializeToFile(value)));
                return o;
            }

            @Override
            public @Nullable DynamicItemPersistentTags deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                Map<Object, DynamicPersistentTag> tags = new HashMap<>();
                o.forEach((key, value) ->{
                    DynamicPersistentTag tag = FileDynamicPersistentTag.deserialize(
                        context, value, key
                    );
                    if(tag==null) return;
                    tags.put(key, tag);
                });
                if(tags.isEmpty()) return null;
                return new DynamicItemPersistentTags(tags);
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
        if(item.components() != null){
            item.components().forEach((key, value) ->{
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
        return null;
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
        return stack;

        /*item.editMeta(meta ->{
            ItemRarity rarity = registry.deserialize(ItemRarity.class, o.get("rarity"));
            if(rarity != null) meta.setRarity(rarity);
        });
        return item.item();*/
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "dynamic_item";
    }
}
