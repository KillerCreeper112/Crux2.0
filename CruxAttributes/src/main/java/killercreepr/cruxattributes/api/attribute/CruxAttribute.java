package killercreepr.cruxattributes.api.attribute;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxattributes.core.attribute.GenericAttribute;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.*;

public interface CruxAttribute extends Keyed {
    static NamespacedKey k(String s){
        return Crux.key(s);
    }

    CruxAttribute ATTACK_DAMAGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_damage")));
    CruxAttribute ATTACK_SPEED = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_speed")){
        @Override
        public double processValue(double value) {
            return value * -1;
        }
    });
    CruxAttribute ATTACK_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_range")));
    CruxAttribute ATTACK_AOE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_aoe")));
    CruxAttribute ATTACK_PIERCE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_pierce")));

    CruxAttribute ATTACK_KNOCKBACK = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_knockback")));
    CruxAttribute ATTACK_KNOCKBACK_UP = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_upwards_knockback")));
    CruxAttribute ARMOR = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("armor")));
    CruxAttribute ARMOR_TOUGHNESS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("armor_toughness")));
    CruxAttribute KNOCKBACK_RESISTANCE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("knockback_resistance")));
    CruxAttribute PROJECTILE_RICOCHET_ENTITY = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("projectile_ricochet_entity")));
    CruxAttribute PROJECTILE_RICOCHET_BLOCK = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("projectile_ricochet_block")));
    CruxAttribute PROJECTILE_RICOCHET_ENTITY_VELOCITY_DROP_OFF = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("projectile_ricochet_entity_velocity_drop_off")));
    CruxAttribute PROJECTILE_RICOCHET_BLOCK_VELOCITY_DROP_OFF = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("projectile_ricochet_block_velocity_drop_off")));

    CruxAttribute MOVEMENT_SPEED = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("movement_speed")));
    CruxAttribute TARGET_HALT_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("target_halt_range")));
    CruxAttribute FIND_TARGET_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("find_target_range")));

    default double getDefaultValue(){
        return 0D;
    }
    default @NotNull String getName(){
        return CruxString.toTitleCase(key().value());
    }

    default boolean isNegative(@NotNull CruxAttributeModifier modifier){
        return isNegative(modifier.getAmount());
    }

    default boolean isNegative(double value){
        return value < 0D;
    }
    default int getRoundMultiple(){ return 1; }

    /**
     * attribute.equals(CruxAttribute.ATTACK_SPEED) ? value*-1D : value;
     */
    default double processValue(double value){
        return value;
    }

    default double round(double value){ return killercreepr.crux.core.util.CruxMath.round(value, getRoundMultiple()); }
    default double round(double value, @NotNull RoundingMode roundingMode){ return CruxMath.round(value, getRoundMultiple(), roundingMode); }

    static ItemStack updateItem(@Nullable ItemStack i){
        if(CruxItem.isEmpty(i)) return i;
        ItemMeta meta = i.getItemMeta();
        if(meta != null) i.setItemMeta(updateMeta(meta));
        return i;
    }

    static ItemMeta updateMeta(@Nullable ItemMeta meta){
        if(meta == null) return null;
        if(hasAttributeData(meta)){
            boolean add = true;
            Key disableVanillaKey = Crux.key("disable_vanilla");
            for(Map.Entry<Attribute, AttributeModifier> m : meta.getAttributeModifiers(EquipmentSlot.OFF_HAND).entries()){
                if(m.getValue().key().equals(disableVanillaKey)){
                    add = false;
                    break;
                }
            }
            if(add){
                meta.addAttributeModifier(Attribute.LUCK, new AttributeModifier(Crux.key("disable_vanilla"), 0D,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.OFFHAND));
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
        }else{
            for(Map.Entry<Attribute, AttributeModifier> m : meta.getAttributeModifiers(EquipmentSlot.OFF_HAND).entries()){
                if(m.getValue().getName().equalsIgnoreCase("crux.disable_vanilla")){
                    meta.removeAttributeModifier(m.getKey(), m.getValue());
                }
            }
            meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        return meta;
    }

    /**
     * Convenience method for GrimAttribute#get(PersistentDataHolder, GrimAttribute)
     */
    default <P extends PersistentDataHolder> double get(@Nullable P e){
        return get(e, this);
    }

    static @NotNull Collection<CruxAttributeInstance>
    getInstances(@Nullable ItemStack i){
        if(CruxItem.isEmpty(i)) return new ArrayList<>();
        return getInstances(i.getItemMeta(), (CruxSlot[]) null);
    }

    static @NotNull Collection<CruxAttributeInstance>
    getInstances(@Nullable ItemStack i, @Nullable CruxSlot @Nullable... slots){
        if(CruxItem.isEmpty(i)) return new ArrayList<>();
        return getInstances(i.getItemMeta(), slots);
    }

    static
    double get(@Nullable ItemStack i, @NotNull CruxAttribute attribute, @Nullable CruxSlot @Nullable... slots){
        return get(i, attribute, 0D, slots);
    }

    static
    double get(@Nullable ItemStack i, @NotNull CruxAttribute attribute){
        return get(i, attribute, 0D, (CruxSlot[]) null);
    }

    static
    double get(@Nullable ItemStack i, @NotNull CruxAttribute attribute, double defaultValue){
        return get(i, attribute, defaultValue, (CruxSlot[]) null);
    }

    static
    double get(@Nullable ItemStack i, @NotNull CruxAttribute attribute, double defaultValue, @Nullable CruxSlot @Nullable... slots){
        if(CruxItem.isEmpty(i)) return defaultValue;
        return get(i.getItemMeta(), attribute, defaultValue, slots);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute){
        return get(i, attribute, 0D, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, @Nullable CruxSlot @Nullable... slots){
        return get(i, attribute, 0D, slots);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, double defaultValue){
        return get(i, attribute, defaultValue, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, double defaultValue, @Nullable CruxSlot @Nullable... slots){
        CruxAttributeInstance x = getInstance(i, attribute, slots);
        if(x == null) return defaultValue;
        return x.getValue();
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull Operation@Nullable... operations){
        return get(i, attribute, 0D, operations);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, double defaultValue, @NotNull Operation@Nullable... operations){
        CruxAttributeInstance x = getInstance(i, attribute, null, operations == null ? null : Set.of(operations));
        if(x == null) return defaultValue;
        return x.getValue();
    }

    static <P extends PersistentDataHolder> boolean hasAttributeData(@Nullable P i){
        PersistentDataContainer components = getComponents(i);
        if(components == null) return false;
        PersistentDataContainer container = getContainer(components);
        return container != null && !container.isEmpty();
    }

    static <P extends PersistentDataHolder> P clearAttributes(@Nullable P i){
        if(i == null) return null;
        PersistentDataContainer components = getComponents(i);
        if(components == null) return i;
        components.remove(k("attributes"));
        i.getPersistentDataContainer().set(k("components"), PersistentDataType.TAG_CONTAINER, components);
        return i;
    }

    static <P extends PersistentDataHolder> P clearModifiers(@Nullable P i, @NotNull CruxAttribute attribute){
        PersistentDataContainer components = getComponents(i);
        if(components == null) return i;
        PersistentDataContainer container = getContainer(components);
        if(container == null) return i;
        container.remove(killercreepr.crux.core.util.CruxKey.key(attribute.key()));
        if(container.isEmpty()) i.getPersistentDataContainer().remove(k("attributes"));
        else i.getPersistentDataContainer().set(k("attributes"), PersistentDataType.TAG_CONTAINER, container);
        return i;
    }

    static ItemStack removeModifiers(@Nullable ItemStack i, @NotNull Key @NotNull... path){
        if(CruxItem.isEmpty(i)) return i;
        ItemMeta meta = i.getItemMeta();
        removeModifiers(meta, path);
        i.setItemMeta(meta);
        return i;
    }

    /**
     * Removes any modifiers in all attributes from the path specified.
     */
    static <P extends PersistentDataHolder> P removeModifiers(@Nullable P i,
                                                                     @NotNull Key @NotNull... path){
        if(i == null || path.length < 1) return i;
        PersistentDataContainer components = getComponents(i);
        if(components==null) return i;
        PersistentDataContainer container = getContainer(components);
        if(container == null) return i;

        for(NamespacedKey k : container.getKeys()){
            CruxAttribute a = CruxAttributeRegistries.ATTRIBUTES.get(k);
            if(a != null) removeModifier(container, a, path);
        }
        components.set(k("attributes"), PersistentDataType.TAG_CONTAINER, container);
        CruxPersist.COMPONENTS.set(i, components);
        return i;
    }

    static ItemStack removeModifier(@Nullable ItemStack i, @NotNull CruxAttribute attribute,
                                                                    @NotNull Key @NotNull... path){
        if(CruxItem.isEmpty(i)) return i;
        ItemMeta meta = i.getItemMeta();
        removeModifier(meta, attribute, path);
        i.setItemMeta(meta);
        return i;
    }

    /**
     * Removes an attribute modifier from the path specified.
     */
    static <P extends PersistentDataHolder> P removeModifier(@Nullable P i, @NotNull CruxAttribute attribute,
                                                                    @NotNull Key @NotNull... path){
        if(i == null || path.length < 1) return i;
        PersistentDataContainer components = getComponents(i);
        if(components == null) return i;
        PersistentDataContainer container = getContainer(components);
        if(container == null) return i;

        removeModifier(container, attribute, path);

        components.set(k("attributes"), PersistentDataType.TAG_CONTAINER, container);
        CruxPersist.COMPONENTS.set(i, components);
        return i;
    }

    @ApiStatus.Experimental
    static <P extends PersistentDataContainer> P removeModifier(@Nullable P base, @NotNull CruxAttribute attribute,
                                                                @NotNull Key @NotNull... path){
        if(base == null || path.length < 1) return base;

        PersistentDataContainer attributeContainer = getAttributeContainerFromBase(base, attribute);
        if(attributeContainer == null || attributeContainer.isEmpty()) return base;

        List<PersistentDataContainer> list = new ArrayList<>();
        int index = 0;
        PersistentDataContainer current = attributeContainer;
        //Get the existing path.
        for(Key pathKey : path){
            index++;
            if(index == path.length){
                current.remove(killercreepr.crux.core.util.CruxKey.key(pathKey));
                break;
            }
            PersistentDataContainer found = null;
            for(NamespacedKey k : current.getKeys()){
                if(k.equals(pathKey)){
                    try{
                        found = current.get(k, PersistentDataType.TAG_CONTAINER);
                        break;
                    }catch (Exception ignored){}
                }
            }
            if(found == null) return base;
            list.add(found);
            current = found;
        }
        if(!list.isEmpty()){
            //enchants -> nothing_here
            index = list.size()-1;
            PersistentDataContainer last = null;
            //Set the path data back into each other. 4 into 3, 3 into 2, 2 into 1.
            for(; index >= 0; index--) {
                PersistentDataContainer c = list.get(index);
                if (last == null) {
                    last = c;
                    continue;
                }
                if(last.isEmpty()) c.remove(killercreepr.crux.core.util.CruxKey.key(path[index+1]));
                else c.set(killercreepr.crux.core.util.CruxKey.key(path[index+1]), PersistentDataType.TAG_CONTAINER, last);
                last = c;
            }
            //Finally, set the whole path into the attribute container.
            if(list.getFirst().isEmpty()) attributeContainer.remove(killercreepr.crux.core.util.CruxKey.key(path[0]));
            else attributeContainer.set(killercreepr.crux.core.util.CruxKey.key(path[0]), PersistentDataType.TAG_CONTAINER, list.getFirst());
        }

        if(attributeContainer.isEmpty()) base.remove(killercreepr.crux.core.util.CruxKey.key(attribute.key()));
        else base.set(killercreepr.crux.core.util.CruxKey.key(attribute.key()), PersistentDataType.TAG_CONTAINER, attributeContainer);

        return base;
    }


    static ItemStack addModifier(@Nullable ItemStack i, @NotNull CruxAttribute attribute,
                                 @NotNull CruxAttributeModifier modifier, @NotNull Key... path){
        if(CruxItem.isEmpty(i)) return i;
        ItemMeta meta = i.getItemMeta();
        addModifier(meta, attribute, modifier, path);
        i.setItemMeta(meta);
        return i;
    }

    static <P extends PersistentDataHolder> P copy(@Nullable P to, @Nullable P from, @NotNull CruxAttribute @Nullable... filter){
        for(CruxAttributeInstance a : getInstances(from)){
            if(filter != null && Arrays.stream(filter).noneMatch(x -> x.equals(a.getAttribute()))) continue;
            for(CruxAttributeModifier m : a.getModifiers()){
                addModifier(to, a.getAttribute(), m);
            }
        }
        return to;
    }


    /**
     * Adds an attribute modifier or overwrites it, if using the same key.
     * If a path is provided, it will nest the attribute modifier.
     */
    static <P extends PersistentDataHolder> P addModifier(@Nullable P i, @NotNull CruxAttribute attribute,
                                                          @NotNull CruxAttributeModifier modifier, @NotNull Key... path){
        if(i == null) return null;
        PersistentDataContainer components = getComponents(i);
        if(components == null) components = i.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
        PersistentDataContainer container = getContainer(components);
        if(container == null) container = i.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();

        addModifier(container, attribute, modifier, path);

        components.set(k("attributes"), PersistentDataType.TAG_CONTAINER, container);
        CruxPersist.COMPONENTS.set(i, components);
        return i;
    }

    @ApiStatus.Experimental
    static <P extends PersistentDataContainer> P addModifier(@Nullable P base, @NotNull CruxAttribute attribute,
                                                          @NotNull CruxAttributeModifier modifier, @NotNull Key... path){
        if(base == null) return null;
        if(path == null || path.length==0) path = modifier.getPath();

        PersistentDataContainer attributeContainer = getAttributeContainer(base, attribute);
        if(attributeContainer == null) attributeContainer = base.getAdapterContext().newPersistentDataContainer();

        PersistentDataContainer modProvider = path == null || path.length < 1 ?
            CruxTag.get(attributeContainer, modifier.key(), PersistentDataType.TAG_CONTAINER, null) :
            getModifierProvider(base, path);
        if(modProvider == null) modProvider = base.getAdapterContext().newPersistentDataContainer();
        modProvider.set(k("value"), PersistentDataType.DOUBLE, modifier.getAmount());
        modProvider.set(k("operation"), PersistentDataType.STRING, modifier.getOperation().toString().toLowerCase());
        if(modifier.getSlot() != null) modProvider.set(k("slot"), PersistentDataType.STRING, modifier.getSlot().toString().toLowerCase());

        //no path provided
        if(path == null || path.length < 1){
            attributeContainer.set(killercreepr.crux.core.util.CruxKey.key(modifier.key()), PersistentDataType.TAG_CONTAINER, modProvider);
        }else{
            List<PersistentDataContainer> list = new ArrayList<>();
            int index = 0;
            PersistentDataContainer current = attributeContainer;
            //Get the existing path or create a new one.
            for(Key pathKey : path){
                index++;
                PersistentDataContainer found = CruxTag.get(current, pathKey, PersistentDataType.TAG_CONTAINER, null);
                if(found == null) found = base.getAdapterContext().newPersistentDataContainer();
                if(index == path.length) found.set(killercreepr.crux.core.util.CruxKey.key(modifier.key()), PersistentDataType.TAG_CONTAINER, modProvider);
                list.add(found);
                current = found;
            }
            //Set the path data back into each other. 4 into 3, 3 into 2, 2 into 1.
            if(path.length > 1){
                index = path.length-1;
                for(; index >= 0; index--) {
                    if(index == (path.length-1)) continue;
                    PersistentDataContainer c = list.get(index);
                    PersistentDataContainer last = list.get(index+1);
                    c.set(CruxKey.key(path[index+1]), PersistentDataType.TAG_CONTAINER, last);
                }
            }
            //Finally, set the whole path into the attribute container.
            attributeContainer.set(killercreepr.crux.core.util.CruxKey.key(path[0]), PersistentDataType.TAG_CONTAINER, list.getFirst());
        }

        base.set(killercreepr.crux.core.util.CruxKey.key(attribute.key()), PersistentDataType.TAG_CONTAINER, attributeContainer);
        return base;
    }

    static  @NotNull Collection<CruxAttributeModifier>
    getModifiers(@Nullable ItemStack i, @NotNull CruxAttribute attribute){
        return getModifiers(i, attribute, (CruxSlot[]) null);
    }

    static  @NotNull Collection<CruxAttributeModifier>
    getModifiers(@Nullable ItemStack i, @NotNull CruxAttribute attribute, @Nullable CruxSlot @Nullable... slots){
        if(CruxItem.isEmpty(i)) return new ArrayList<>();
        return getModifiers(i.getItemMeta(), attribute, slots);
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeModifier>
    getModifiers(@Nullable P i, @NotNull CruxAttribute attribute){
        return getModifiers(i, attribute, (CruxSlot[]) null);
    }

    static @NotNull Collection<CruxAttributeModifier>
    getModifiers(@Nullable ItemStack i, @NotNull CruxAttribute attribute, @NotNull Key@NotNull... path){
        Collection<CruxAttributeModifier> list = getModifiers(i, attribute);
        list.removeIf(x -> !Arrays.equals(x.getPath(), path));
        return list;
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeModifier>
    getModifiers(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull Key@NotNull... path){
        Collection<CruxAttributeModifier> list = getModifiers(i, attribute);
        list.removeIf(x -> !Arrays.equals(x.getPath(), path));
        return list;
    }

    static <P extends PersistentDataHolder> @Nullable CruxAttributeModifier
    getModifier(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull Key modKey){
        return getModifier(i, attribute, modKey, (Key[]) null);
    }

    static <P extends PersistentDataHolder> @Nullable CruxAttributeModifier
    getModifier(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull Key modKey, @NotNull Key@Nullable... path){
        Collection<CruxAttributeModifier> list = getModifiers(i, attribute);
        for(CruxAttributeModifier m :list){
            if(m.key().equals(modKey)){
                if(Arrays.equals(m.getPath(), path)) return m;
            }
        }
        return null;
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeModifier>
    getModifiers(@Nullable P i, @NotNull CruxAttribute attribute, @Nullable CruxSlot @Nullable... slots){
        PersistentDataContainer components = getComponents(i);
        if(components==null) return Set.of();
        Collection<CruxAttributeModifier> list = new ArrayList<>();
        PersistentDataContainer attributeContainer = getAttributeContainer(components, attribute);
        if(attributeContainer == null || i == null) return list;
        list.addAll(convertToModifiers(attributeContainer));
        if(slots != null){
            list.removeIf(e -> Arrays.stream(slots).noneMatch(x -> x == e.getSlot()));
        }
        return list;
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeInstance>
    getInstances(@Nullable P i){
        return getInstances(i, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeInstance>
    getInstances(@Nullable P i, @Nullable CruxSlot @Nullable... slots){
        PersistentDataContainer components = getComponents(i);
        if(components==null) return Set.of();
        PersistentDataContainer container = getContainer(components);
        if(container == null) return Set.of();
        Collection<CruxAttributeInstance> list = new HashSet<>();
        for(NamespacedKey k : container.getKeys()){
            CruxAttribute attribute = CruxAttributeRegistries.ATTRIBUTES.get(k);
            if(attribute == null) continue;
            CruxAttributeInstance instance = getInstance(i, attribute, slots);
            if(instance != null) list.add(instance);
        }
        return list;
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstance(@Nullable P i, @NotNull CruxAttribute attribute){
        return getInstance(i, attribute, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstance(@Nullable P i, @NotNull CruxAttribute attribute, @Nullable CruxSlot @Nullable... slots){
        PersistentDataContainer components = getComponents(i);
        if(components==null) return null;
        PersistentDataContainer modifierProvider = getAttributeContainer(components, attribute);
        if(modifierProvider == null) return null;
        Collection<CruxAttributeModifier> list = convertToModifiers(modifierProvider);
        if(slots != null){
            list.removeIf(e -> Arrays.stream(slots).noneMatch(x -> x == e.getSlot()));
        }
        return CruxAttributeInstance.instance(attribute, list);
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstance(@Nullable P i, @NotNull CruxAttribute attribute, @Nullable Collection<@Nullable CruxSlot> slots,
                                                      @Nullable Collection<@NotNull Operation> operations){
        PersistentDataContainer components = getComponents(i);
        if(components==null) return null;
        PersistentDataContainer container = getContainer(components);
        if(container == null) return null;
        PersistentDataContainer modifierProvider = getAttributeContainer(container, attribute);
        if(modifierProvider == null) return null;
        Collection<CruxAttributeModifier> list = convertToModifiers(modifierProvider);
        if(slots != null){
            list.removeIf(e -> !slots.contains(e.getSlot()));
        }
        if(operations != null){
            list.removeIf(e -> !operations.contains(e.getOperation()));
        }
        return CruxAttributeInstance.instance(attribute, list);
    }

    /*static <P extends PersistentDataHolder>
    @Nullable PersistentDataContainer getContainer(@Nullable P i){
        return killercreepr.crux.core.util.CruxTag.get(i, "attributes", PersistentDataType.TAG_CONTAINER, null);
    }*/

    static <P extends PersistentDataHolder>
    @Nullable PersistentDataContainer getComponents(@Nullable P i){
        return killercreepr.crux.core.util.CruxTag.get(i, "components", PersistentDataType.TAG_CONTAINER, null);
    }

    static <P extends PersistentDataContainer>
    @Nullable PersistentDataContainer getContainer(@Nullable P i){
        return killercreepr.crux.core.util.CruxTag.get(i, "attributes", PersistentDataType.TAG_CONTAINER, null);
    }

    static <P extends PersistentDataHolder>
    @Nullable PersistentDataContainer  getAttributeContainer(@Nullable P i, @NotNull CruxAttribute attribute){
        return getAttributeContainer(i == null ? null : i.getPersistentDataContainer(), attribute);
    }

    static <P extends PersistentDataContainer>
    @Nullable PersistentDataContainer  getAttributeContainer(@Nullable P i, @NotNull CruxAttribute attribute){
        PersistentDataContainer container = getContainer(i);
        if(container == null) return null;
        return getAttributeContainerFromBase(container, attribute);
    }

    static <P extends PersistentDataContainer>
    @Nullable PersistentDataContainer  getAttributeContainerFromBase(@Nullable P i, @NotNull CruxAttribute attribute){
        return CruxTag.get(i, attribute.key(), PersistentDataType.TAG_CONTAINER, null);
    }

    static <P extends PersistentDataHolder>
    @Nullable PersistentDataContainer getModifierProvider(@Nullable P i, @NotNull Key @NotNull... path){
        return getModifierProvider(i, 0, path);
    }

    static <P extends PersistentDataContainer>
    @Nullable PersistentDataContainer getModifierProvider(@Nullable P i, @NotNull Key @NotNull... path){
        return getModifierProvider(i, 0, path);
    }
    static <P extends PersistentDataContainer>
    @Nullable PersistentDataContainer getModifierProvider(@Nullable P i, int index, @NotNull Key @NotNull... path){
        if(i == null || index < 0 || index >= path.length) return null;
        Key key = path[index];
        for(Key keyed : path){
            NamespacedKey k = CruxKey.key(keyed);
            if(k.equals(key) && i.has(k, PersistentDataType.TAG_CONTAINER)){
                PersistentDataContainer c = i.get(k, PersistentDataType.TAG_CONTAINER);
                if(c == null) return null;
                return getModifierProvider(i, index+1, path);
            }
        }
        return null;
    }

    static <P extends PersistentDataHolder>
    @Nullable PersistentDataContainer getModifierProvider(@Nullable P i, int index, @NotNull Key @NotNull... path){
        return getModifierProvider(i == null ? null : i.getPersistentDataContainer(), index, path);
    }

    static <P extends PersistentDataHolder>
    @Nullable PersistentDataContainer getModifierContainer(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull NamespacedKey key){
        PersistentDataContainer attributeContainer = getAttributeContainer(i, attribute);
        if(attributeContainer == null) return null;
        try{ return attributeContainer.get(key, PersistentDataType.TAG_CONTAINER); }
        catch (Exception ex){ return null; }
    }

    static @NotNull List<CruxAttributeModifier> convertToModifiers(@Nullable PersistentDataContainer modProvider){
        return convertToModifiers(modProvider, null);
    }

    static @NotNull List<CruxAttributeModifier> convertToModifiers(@Nullable PersistentDataContainer modProvider, @Nullable List<NamespacedKey> path){
        List<CruxAttributeModifier> list = new ArrayList<>();
        if(modProvider == null) return list;
        for(NamespacedKey k : modProvider.getKeys()){
            if(!modProvider.has(k, PersistentDataType.TAG_CONTAINER)) continue;
            PersistentDataContainer provider = modProvider.get(k, PersistentDataType.TAG_CONTAINER);
            if(provider == null) continue;
            CruxAttributeModifier mod = convertToModifier(provider, k);
            //provider nested in provider
            if(mod == null){
                List<NamespacedKey> newPath = path == null ? new ArrayList<>() : new ArrayList<>(path);
                newPath.add(k);
                list.addAll(convertToModifiers(provider, newPath));
            } else{
                list.add(mod);
                if(path != null) mod.setPath(path.toArray(new NamespacedKey[0]));
            }
        }
        return list;
    }

    static @Nullable CruxAttributeModifier convertToModifier(@Nullable PersistentDataContainer modContainer,
                                                                   @NotNull NamespacedKey key){
        if(modContainer == null || !modContainer.has(k("value"), PersistentDataType.DOUBLE)) return null;
        Operation o;
        try{
            o = Operation.valueOf(modContainer.getOrDefault(k("operation"), PersistentDataType.STRING, "ADD").toUpperCase());
        }catch (IllegalArgumentException e) { o = Operation.ADD; }
        CruxSlot slot;
        try{
            slot = CruxSlot.valueOf(modContainer.getOrDefault(k("slot"), PersistentDataType.STRING, "").toUpperCase());
        }catch (IllegalArgumentException e) { slot = null; }
        return CruxAttributeModifier.modifier(key, modContainer.getOrDefault(k("value"), PersistentDataType.DOUBLE, 0D), o, slot);
    }

    static @NotNull List<PersistentDataContainer> getModifierProviders(@Nullable PersistentDataContainer attributeOrModContainer){
        List<PersistentDataContainer> list = new ArrayList<>();
        if(attributeOrModContainer == null) return list;
        for(NamespacedKey k : attributeOrModContainer.getKeys()){
            if(attributeOrModContainer.has(k, PersistentDataType.TAG_CONTAINER)){
                PersistentDataContainer provider = attributeOrModContainer.get(k, PersistentDataType.TAG_CONTAINER);
                if(provider != null && convertToModifier(provider, k) == null) list.add(provider);
            }
        }
        return list;
    }

    enum Operation{
        ADD,
        MULTIPLY,
        SET;

        public String id(){
            return this.toString().toLowerCase();
        }

        public static @Nullable Operation match(@NotNull String id){
            try{
                return Operation.valueOf(id.toUpperCase());
            }catch (IllegalArgumentException ignored){ return null; }
        }
    }
}
