package killercreepr.cruxattributes.api.attribute;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.attribute.GenericAttribute;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
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

public interface CruxAttribute extends CruxKeyed, Comparable<CruxAttribute> {
    static NamespacedKey k(String s){
        return Crux.key(s);
    }

    CruxAttribute SHIELD_PIERCING = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("shield_piercing")));
    CruxAttribute NATURAL_HEAL_BONUS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("natural_heal_bonus")){
        @Override
        public String formatValue(double amount) {
            return super.formatValue(amount*100D);
        }
    });
    CruxAttribute FOOD_SATURATION_BONUS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("food_saturation_bonus")){
        @Override
        public String formatValue(double amount) {
            return super.formatValue(amount*100D);
        }
    });

    CruxAttribute ATTACK_DAMAGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_damage")));
    CruxAttribute ATTACK_SPEED = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_speed")){
        @Override
        public double processValue(double value) {
            return value * -1;
        }

        @Override
        public boolean isNegative(double value) {
            return value > 0D;
        }
    });
    CruxAttribute ATTACK_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_range")));
    CruxAttribute ATTACK_AOE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_aoe")));
    CruxAttribute ATTACK_PIERCE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_pierce")));
    CruxAttribute ARMOR_PENETRATION = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("armor_penetration")));

    CruxAttribute MAX_HEALTH = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("max_health")));
    CruxAttribute MAX_ABSORPTION = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("max_absorption")));
    CruxAttribute LUCK = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("luck")));
    CruxAttribute BLOCK_BREAK_SPEED = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("block_break_speed")));


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

    CruxAttribute MAGIC_POTENCY = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("magic_potency")));

    CruxAttribute FOLLOW_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("follow_range")));
    CruxAttribute FLYING_SPEED = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("flying_speed")));
    CruxAttribute FALL_DAMAGE_MULTIPLIER = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("fall_damage_multiplier")));
    CruxAttribute SAFE_FALL_DISTANCE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("safe_fall_distance")));
    CruxAttribute SCALE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("scale")));
    CruxAttribute STEP_HEIGHT = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("step_height")));
    CruxAttribute GRAVITY = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("gravity")));
    CruxAttribute JUMP_STRENGTH = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("jump_strength")));
    CruxAttribute BURNING_TIME = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("burning_time")));
    CruxAttribute EXPLOSION_KNOCKBACK_RESISTANCE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("explosion_knockback_resistance")));
    CruxAttribute MOVEMENT_EFFICIENCY = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("movement_efficiency")));
    CruxAttribute OXYGEN_BONUS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("oxygen_bonus")));
    CruxAttribute WATER_MOVEMENT_EFFICIENCY = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("water_movement_efficiency")));
    CruxAttribute TEMPT_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("tempt_range")));
    CruxAttribute BLOCK_INTERACTION_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("block_interaction_range")));
    CruxAttribute ENTITY_INTERACTION_RANGE = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("entity_interaction_range")));
    CruxAttribute MINING_EFFICIENCY = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("mining_efficiency")));
    CruxAttribute SNEAKING_SPEED = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("sneaking_speed")));
    CruxAttribute SUBMERGED_MINING_SPEED = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("submerged_mining_speed")));
    CruxAttribute SWEEPING_DAMAGE_RATIO = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("sweeping_damage_ratio")));
    CruxAttribute SPAWN_REINFORCEMENTS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("spawn_reinforcements")));

    CruxAttribute PUSHBACK = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("pushback")));
    CruxAttribute UPWARDS_PUSHBACK = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("upwards_pushback")));

    CruxAttribute IMPACT_EFFECTIVENESS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("impact_effectiveness")));

    CruxAttribute ATTACK_FREEZE_TICKS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_freeze_ticks")));
    CruxAttribute ATTACK_FIRE_TICKS = CruxAttributeRegistries.ATTRIBUTES.register(new GenericAttribute(k("attack_fire_ticks")));

    default double getDefaultValue(){
        return 0D;
    }
    default @NotNull String getName(){
        return CruxString.toTitleCase(key().value());
    }

    default boolean isNegative(@NotNull CruxAttributeModifier modifier){
        return isNegative(modifier.getAmount());
    }

    default String formatValue(double amount){
        return CruxMath.format(processValue(amount));
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

    static <P extends PersistentDataHolder> CruxAttributeHandler getOrCreateCache(P item){
        return CruxAttributeCacheHandler.attributeCacheHandler().getOrCreateCache(item);
    }
    static <P extends PersistentDataHolder> CruxAttributeHandler getCache(P item){
        return CruxAttributeCacheHandler.attributeCacheHandler().getCache(item);
    }

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
    getInstances(@Nullable ItemStack i, @NotNull CruxSlot @Nullable... slots){
        if(CruxItem.isEmpty(i)) return new ArrayList<>();
        return getInstances(i.getItemMeta(), slots);
    }

    static
    double get(@Nullable ItemStack i, @NotNull CruxAttribute attribute, @NotNull CruxSlot @Nullable... slots){
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
    double get(@Nullable ItemStack i, @NotNull CruxAttribute attribute, double defaultValue, @NotNull CruxSlot @Nullable... slots){
        if(CruxItem.isEmpty(i)) return defaultValue;
        return get(i.getItemMeta(), attribute, defaultValue, slots);
    }

    static <P extends ItemStack> P applyAttributes(@Nullable P i, @NotNull CruxAttributeHandler attributes){
        return applyAttributes(i, attributes.getInstances());
    }

    static <P extends ItemStack> P applyAttributes(@Nullable P i, @NotNull Collection<? extends CruxAttributeInstance> attributes){
        if(i ==null) return null;
        for(CruxAttributeInstance in : attributes){
            for(CruxAttributeModifier mod : in.getModifiers()){
                addModifier(i, in.getAttribute(), mod);
            }
        }
        return i;
    }

    static <P extends PersistentDataHolder> P applyAttributes(@Nullable P i, @NotNull CruxAttributeHandler attributes){
        return applyAttributes(i, attributes.getInstances());
    }

    static <P extends PersistentDataHolder> P applyAttributes(@Nullable P i, @NotNull Collection<? extends CruxAttributeInstance> attributes){
        if(i ==null) return null;
        for(CruxAttributeInstance in : attributes){
            for(CruxAttributeModifier mod : in.getModifiers()){
                addModifier(i, in.getAttribute(), mod);
            }
        }
        return i;
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute){
        return get(i, attribute, 0D, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull CruxSlot @Nullable... slots){
        return get(i, attribute, 0D, slots);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, double defaultValue){
        return get(i, attribute, defaultValue, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder>
    double get(@Nullable P i, @NotNull CruxAttribute attribute, double defaultValue, @NotNull CruxSlot @Nullable... slots){
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
        if(i==null) return false;

        var cache = getCache(i);
        if(cache != null) return !cache.getInstances().isEmpty();

        PersistentDataContainer components = getComponents(i);
        if(components == null) return false;
        PersistentDataContainer container = getContainer(components);
        return container != null && !container.isEmpty();
    }

    static <P extends PersistentDataHolder> boolean hasAttributeData(@Nullable P i, @NotNull CruxAttribute attribute){
        if(i==null) return false;

        var cache = getOrCreateCache(i);
        if(cache != null) return cache.getInstance(attribute) != null && !cache.getInstance(attribute).isEmpty();

        PersistentDataContainer components = getComponents(i);
        if(components == null) return false;
        PersistentDataContainer container = getContainer(components);
        return container != null && getAttributeContainerFromBase(container, attribute) != null;
    }

    static <P extends PersistentDataHolder> P clearAttributes(@Nullable P i){
        if(i == null) return i;
        var cache = getOrCreateCache(i);
        if(cache != null){
            cache.clearAllModifiers();
            return i;
        }

        return clearAttributesRaw(i);
    }

    static <P extends PersistentDataHolder> P clearAttributesRaw(@Nullable P i){
        if(i == null) return null;
        PersistentDataContainer components = getComponents(i);
        if(components == null) return i;
        components.remove(k("attributes"));
        i.getPersistentDataContainer().set(k("components"), PersistentDataType.TAG_CONTAINER, components);
        return i;
    }

    static <P extends PersistentDataHolder> P clearModifiers(@Nullable P i, @NotNull CruxAttribute attribute){
        if(i == null) return i;
        var cache = getOrCreateCache(i);
        if(cache != null){
            cache.clearModifiers(attribute);
            return i;
        }
        return clearModifiersRaw(i, attribute);
    }

    static <P extends PersistentDataHolder> P clearModifiersRaw(@Nullable P i, @NotNull CruxAttribute attribute){
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
        var cache = getOrCreateCache(i);
        if(cache != null){
            cache.removeModifiers(path);
            return i;
        }

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

        var cache = getOrCreateCache(i);
        if(cache != null){
            cache.removeModifier(attribute, path);
            return i;
        }

        return removeModifierRaw(i, attribute, path);
    }

    static <P extends PersistentDataHolder> P removeModifierRaw(@Nullable P i, @NotNull CruxAttribute attribute,
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

        var cache = getOrCreateCache(i);
        if(cache != null){
            if (path == null || path.length == 0) {
                path = modifier.getPath();
            }

            cache.addModifier(attribute, modifier.withPath(path));
            return i;
        }

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

        PersistentDataContainer attributeContainer = getAttributeContainerFromBase(base, attribute);
        if(attributeContainer == null) attributeContainer = base.getAdapterContext().newPersistentDataContainer();

        PersistentDataContainer modProvider = path == null || path.length < 1 ?
            CruxTag.get(attributeContainer, modifier.key(), PersistentDataType.TAG_CONTAINER, null) :
            getModifierProvider(base, path);
        if(modProvider == null) modProvider = base.getAdapterContext().newPersistentDataContainer();
        modProvider.set(k("value"), PersistentDataType.DOUBLE, modifier.getAmount());
        modProvider.set(k("operation"), PersistentDataType.STRING, modifier.getOperation().toString().toLowerCase());
        if(!modifier.getSlotGroup().equals(CruxSlotGroup.ANY)){
            modProvider.set(k("slot"), CruxPersistence.CRUX_KEY, modifier.getSlotGroup().key());
        }

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
    getModifiers(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull CruxSlot @Nullable... slots){
        if(i == null) return Set.of();
        var cache = getOrCreateCache(i);
        if(cache != null){
            var instance = cache.getInstance(attribute);
            if(instance == null) return Set.of();

            if(slots != null && slots.length > 0){
                Collection<CruxAttributeModifier> list = new ArrayList<>(instance.getModifiers());
                list.removeIf(e -> !e.getSlotGroup().anyMatch(slots));
                return list;
            }
            return instance.getModifiers();
        }
        return getModifiersRaw(i, attribute, slots);
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeModifier>
    getModifiersRaw(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull CruxSlot @Nullable... slots){
        if(i == null) return Set.of();
        var cache = getOrCreateCache(i);
        if(cache != null){
            var instance = cache.getInstance(attribute);
            if(instance == null) return Set.of();

            if(slots != null && slots.length > 0){
                Collection<CruxAttributeModifier> list = new ArrayList<>(instance.getModifiers());
                list.removeIf(e -> !e.getSlotGroup().anyMatch(slots));
                return list;
            }
            return instance.getModifiers();
        }

        PersistentDataContainer components = getComponents(i);
        if(components==null) return Set.of();
        Collection<CruxAttributeModifier> list = new ArrayList<>();
        PersistentDataContainer attributeContainer = getAttributeContainer(components, attribute);
        if(attributeContainer == null || i == null) return list;
        list.addAll(convertToModifiers(attributeContainer));
        if(slots != null && slots.length > 0){
            list.removeIf(e -> !e.getSlotGroup().anyMatch(slots));
        }
        return list;
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeInstance>
    getInstances(@Nullable P i){
        return getInstances(i, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeInstance>
    getInstancesRaw(@Nullable P i){
        return getInstancesRaw(i, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeInstance>
    getInstances(@Nullable P i, @NotNull CruxSlot @Nullable... slots){
        if(i == null) return Set.of();
        var cache = getOrCreateCache(i);
        if(cache != null){
            if(slots == null || slots.length < 1) return (Collection<CruxAttributeInstance>) cache.getInstances();
            Collection<CruxAttributeInstance> list = new HashSet<>();
            for(var attribute : cache.getInstances()){
                var instance = getInstance(i, attribute.getAttribute(), slots);
                if(instance == null) continue;
                list.add(instance);
            }
            return list;
        }
        return getInstancesRaw(i, slots);
    }
    static <P extends PersistentDataHolder> @NotNull Collection<CruxAttributeInstance>
    getInstancesRaw(@Nullable P i, @NotNull CruxSlot @Nullable... slots){
        PersistentDataContainer components = getComponents(i);
        if(components==null) return Set.of();
        PersistentDataContainer container = getContainer(components);
        if(container == null) return Set.of();
        Collection<CruxAttributeInstance> list = new HashSet<>();
        for(NamespacedKey k : container.getKeys()){
            CruxAttribute attribute = CruxAttributeRegistries.ATTRIBUTES.get(k);
            if(attribute == null) continue;
            CruxAttributeInstance instance = getInstanceRaw(i, attribute, slots);
            if(instance != null) list.add(instance);
        }
        return list;
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstance(@Nullable P i, @NotNull CruxAttribute attribute){
        return getInstance(i, attribute, (CruxSlot[]) null);
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstance(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull CruxSlot @Nullable... slots){
        if(i == null) return null;
        var cache = getOrCreateCache(i);
        if(cache != null){
            var instance = cache.getInstance(attribute);
            if(instance == null) return null;
            if(slots != null && slots.length > 0){
                Collection<CruxAttributeModifier> list = new ArrayList<>(instance.getModifiers());
                list.removeIf(e -> !e.getSlotGroup().anyMatch(slots));
                return CruxAttributeInstance.instance(attribute, list);
            }
            return instance;
        }
        return getInstanceRaw(i, attribute, slots);
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstanceRaw(@Nullable P i, @NotNull CruxAttribute attribute, @NotNull CruxSlot @Nullable... slots){
        PersistentDataContainer components = getComponents(i);
        if(components==null) return null;
        PersistentDataContainer modifierProvider = getAttributeContainer(components, attribute);
        if(modifierProvider == null) return null;
        Collection<CruxAttributeModifier> list = convertToModifiers(modifierProvider);
        if(slots != null && slots.length > 0){
            list.removeIf(e -> !e.getSlotGroup().anyMatch(slots));
        }
        return CruxAttributeInstance.instance(attribute, list);
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstance(@Nullable P i, @NotNull CruxAttribute attribute, @Nullable Collection<@Nullable CruxSlot> slots,
                                                      @Nullable Collection<@NotNull Operation> operations){
        if(i==null) return null;
        var cache = getOrCreateCache(i);
        if(cache != null){
            var instance = cache.getInstance(attribute);
            if(instance == null) return null;

            Collection<CruxAttributeModifier> list = new ArrayList<>(instance.getModifiers());
            if(slots != null && !slots.isEmpty()){
                list.removeIf(e -> !e.getSlotGroup().anyMatch(slots));
            }
            if(operations != null && !operations.isEmpty()){
                list.removeIf(e -> !operations.contains(e.getOperation()));
            }
            return CruxAttributeInstance.instance(attribute, list);
        }
        return getInstanceRaw(i, attribute, slots, operations);
    }

    static <P extends PersistentDataHolder>
    @Nullable CruxAttributeInstance getInstanceRaw(@Nullable P i, @NotNull CruxAttribute attribute, @Nullable Collection<@Nullable CruxSlot> slots,
                                                @Nullable Collection<@NotNull Operation> operations){
        PersistentDataContainer components = getComponents(i);
        if(components==null) return null;
        PersistentDataContainer container = getContainer(components);
        if(container == null) return null;
        PersistentDataContainer modifierProvider = getAttributeContainer(container, attribute);
        if(modifierProvider == null) return null;
        Collection<CruxAttributeModifier> list = convertToModifiers(modifierProvider);
        if(slots != null && !slots.isEmpty()){
            list.removeIf(e -> !e.getSlotGroup().anyMatch(slots));
        }
        if(operations != null && !operations.isEmpty()){
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
                if(path != null) mod = mod.withPath(path.toArray(new NamespacedKey[0]));
                list.add(mod);
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

        Key slotID = CruxTag.get(modContainer, k("slot"), CruxPersistence.CRUX_KEY, null);

        CruxSlotGroup slot = slotID == null ? null : CruxAttributeRegistries.SLOT_GROUP.get(slotID);
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
