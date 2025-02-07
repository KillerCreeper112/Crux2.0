package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import killercreepr.cruxtickables.core.registries.CruxTickableRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface EntityTickable extends CruxKeyed {
    @Nullable ActiveEntityTickable buildActive(@NotNull Entity user, @Nullable CruxSlot slot);

    /*static <T extends PersistentDataContainer> EntityTickablesContainer getTickables(T base, CruxSlot slot){
        PersistentDataContainer components = getComponents(base);
        if(components==null) return null;
        PersistentDataContainer tickables = getTickablesContainer(components);
        if(tickables==null) return null;
        PersistentDataContainer slotContainer = getSlotContainer(base, slot);
        if(slotContainer==null) return null;
        slotContainer.getKeys().forEach(key ->{
            EntityTickable tickable = CruxTickableRegistries.ENTITY_TICKABLE.get(key);
            if(tickable == null){
                Crux.log(Level.SEVERE, "No tickable found while getting EntityTickable " + key + "!");
                return;
            }
            PersistentDataContainer modContainer = CruxTag.get(slotContainer, key, PersistentDataType.TAG_CONTAINER, null);
            if(modContainer == null) return;
            EntityTickableModifier mod = parseModifier(tickable, modContainer);

        });
    }*/

    static <P extends PersistentDataHolder> P removeModifiers(@Nullable P user,
                                                                       @NotNull Key @NotNull... path){
        if(user == null || path.length < 1) return user;
        PersistentDataContainer components = getComponents(user);
        if(components == null) return user;
        PersistentDataContainer tickables = getTickablesContainer(components);
        if(tickables == null) return user;

        removeModifiersDirect(tickables, path);

        if(tickables.isEmpty()) CruxTag.remove(components, "entity_tickables");
        else CruxTag.set(components, "entity_tickables", PersistentDataType.TAG_CONTAINER, tickables);
        return user;
    }

    static <P extends PersistentDataContainer> P removeModifiersDirect(@Nullable P base,
                                                              @NotNull Key @NotNull... path){
        if(base == null || path.length < 1) return base;

        for(NamespacedKey k : base.getKeys()){
            EntityTickable tickable = CruxTickableRegistries.ENTITY_TICKABLE.get(k);
            if(tickable != null) removeModifierDirect(base, path);
        }
        return base;
    }

    static <P extends PersistentDataContainer> Map<EntityTickable, Collection<EntityTickableModifier>> getTickables(@Nullable P base,
                                                                                               Key... path){
        PersistentDataContainer components = getComponents(base);
        if(components==null) return Map.of();
        PersistentDataContainer tickables = getTickablesContainer(components);
        if(tickables == null) return Map.of();
        return getTickablesDirect(tickables, path);
    }

    static <P extends PersistentDataContainer> Map<EntityTickable, Collection<EntityTickableModifier>> getTickablesDirect(@Nullable P base,
                                                                                                     Key... path){
        if(base==null) return Map.of();
        Map<EntityTickable, Collection<EntityTickableModifier>> map = new HashMap<>();
        for(Key key : base.getKeys()){
            EntityTickable tickable = CruxTickableRegistries.ENTITY_TICKABLE.get(key);
            if(tickable==null) continue;
            PersistentDataContainer tickableContainer = CruxTag.get(base, key, PersistentDataType.TAG_CONTAINER, null);
            if(tickableContainer==null) continue;
            var modifiers = getModifiersDirect(tickableContainer, path);
            if(modifiers.isEmpty()) continue;
            map.put(tickable, modifiers);
        }
        return map;
    }

    static <P extends PersistentDataContainer> Collection<EntityTickableModifier> getModifiers(@Nullable P base,
                                                                                               @NotNull EntityTickable tickable,
                                                                                               Key... path){
        PersistentDataContainer components = getComponents(base);
        if(components==null) return Set.of();
        PersistentDataContainer tickables = getTickablesContainer(components);
        if(tickables == null) return Set.of();
        PersistentDataContainer tickablesContainer = CruxTag.get(tickables, tickable.key(), PersistentDataType.TAG_CONTAINER, null);
        if(tickablesContainer==null) return Set.of();
        return getModifiersDirect(tickablesContainer, path);
    }

    static <P extends PersistentDataContainer> Collection<EntityTickableModifier> getModifiersDirect(@Nullable P base,
                                                                                               Key... path){
        if(base==null) return Set.of();
        PersistentDataContainer modContainer = getModifierProvider(base, path);
        return parseModifiers(modContainer);
    }

    static <P extends PersistentDataContainer> Collection<EntityTickableModifier> getModifiers(@Nullable P base, @NotNull EntityTickable tickable){
        PersistentDataContainer components = getComponents(base);
        if(components==null) return Set.of();
        PersistentDataContainer tickables = getTickablesContainer(components);
        if(tickables == null) return Set.of();
        PersistentDataContainer tickablesContainer = CruxTag.get(tickables, tickable.key(), PersistentDataType.TAG_CONTAINER, null);
        return getModifiersDirect(tickablesContainer);
    }

    static <P extends PersistentDataContainer> Map<EntityTickable, Collection<EntityTickableModifier>> getTickables(@Nullable P base){
        PersistentDataContainer components = getComponents(base);
        if(components==null) return Map.of();
        PersistentDataContainer tickables = getTickablesContainer(components);
        if(tickables == null) return Map.of();
        return getTickablesDirect(tickables);
    }

    static <P extends PersistentDataContainer> Map<EntityTickable, Collection<EntityTickableModifier>> getTickablesDirect(@Nullable P base){
        if(base==null) return Map.of();
        Map<EntityTickable, Collection<EntityTickableModifier>> map = new HashMap<>();
        for(Key key : base.getKeys()){
            EntityTickable tickable = CruxTickableRegistries.ENTITY_TICKABLE.get(key);
            if(tickable == null) continue;
            PersistentDataContainer modContainer = CruxTag.get(base, key, PersistentDataType.TAG_CONTAINER, null);
            Collection<EntityTickableModifier> modifiers = getModifiersDirect(modContainer);
            if(modifiers.isEmpty()) continue;
            map.put(tickable, modifiers);
        }
        return map;
    }

    static <P extends PersistentDataContainer> Collection<EntityTickableModifier> getModifiersDirect(@Nullable P base){
        return parseModifiers(base);
    }

    static <P extends PersistentDataContainer> P removeModifierDirect(@Nullable P base,
                                                                @NotNull Key @NotNull... path){
        if(base == null || path.length < 1) return base;

        List<PersistentDataContainer> list = new ArrayList<>();
        int index = 0;
        PersistentDataContainer current = base;
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
            if(list.getFirst().isEmpty()) base.remove(killercreepr.crux.core.util.CruxKey.key(path[0]));
            else base.set(killercreepr.crux.core.util.CruxKey.key(path[0]), PersistentDataType.TAG_CONTAINER, list.getFirst());
        }

        return base;
    }

    static <P extends PersistentDataContainer> P addModifier(@Nullable P base, @NotNull EntityTickable tickable,
                                                                   @NotNull EntityTickableModifier modifier,
                                                                   @NotNull Key... path){
        if(base == null) return null;
        PersistentDataContainer components = getComponents(base);
        if(components == null) components = base.getAdapterContext().newPersistentDataContainer();
        PersistentDataContainer tickables = getTickablesContainer(components);
        if(tickables == null) tickables = base.getAdapterContext().newPersistentDataContainer();

        addModifierDirect(tickables, tickable, modifier, path);

        CruxTag.set(components, "entity_tickables", PersistentDataType.TAG_CONTAINER, tickables);
        CruxTag.set(base, "components", PersistentDataType.TAG_CONTAINER, components);
        return base;
    }

    static <P extends PersistentDataContainer> P addModifierDirect(@Nullable P base, @NotNull EntityTickable tickable,
                                                                   @NotNull EntityTickableModifier modifier,
                                                                   @NotNull Key... path){
        if(base == null) return null;
        PersistentDataContainer modifiers = getModifierContainer(base, tickable);
        if(modifiers == null) base.getAdapterContext().newPersistentDataContainer();
        addModifierDirect(modifiers, modifier, path);
        CruxTag.set(base, tickable.key(), PersistentDataType.TAG_CONTAINER, modifiers);
        return base;
    }

    @ApiStatus.Experimental
    static <P extends PersistentDataContainer> P addModifierDirect(@Nullable P base, @NotNull EntityTickableModifier modifier, @NotNull Key... path){
        if(base == null) return null;
        if(path == null || path.length==0) path = modifier.getPath();

        PersistentDataContainer modProvider = path == null || path.length < 1 ?
            CruxTag.get(base, modifier.key(), PersistentDataType.TAG_CONTAINER, null) :
            getModifierProvider(base, path);
        if(modProvider == null) modProvider = base.getAdapterContext().newPersistentDataContainer();
        modProvider.set(Crux.key("slot"), CruxPersistence.CRUX_KEY, modifier.getSlotGroup().key());

        //no path provided
        if(path == null || path.length < 1){
            base.set(killercreepr.crux.core.util.CruxKey.key(modifier.key()), PersistentDataType.TAG_CONTAINER, modProvider);
        }else{
            List<PersistentDataContainer> list = new ArrayList<>();
            int index = 0;
            PersistentDataContainer current = base;
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
            //Finally, set the whole path into the tickables container.
            base.set(killercreepr.crux.core.util.CruxKey.key(path[0]), PersistentDataType.TAG_CONTAINER, list.getFirst());
        }
        return base;
    }

    private static <P extends PersistentDataContainer>
    @Nullable PersistentDataContainer getModifierProvider(@Nullable P i, @NotNull Key @NotNull... path){
        return getModifierProvider(i, 0, path);
    }
    private static <P extends PersistentDataContainer>
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

    static @NotNull List<EntityTickableModifier> parseModifiers(@Nullable PersistentDataContainer modProvider){
        return parseModifiers(modProvider, null);
    }

    static @NotNull List<EntityTickableModifier> parseModifiers(@Nullable PersistentDataContainer modProvider, @Nullable List<NamespacedKey> path){
        List<EntityTickableModifier> list = new ArrayList<>();
        if(modProvider == null) return list;
        for(NamespacedKey k : modProvider.getKeys()){
            if(!modProvider.has(k, PersistentDataType.TAG_CONTAINER)) continue;
            PersistentDataContainer provider = modProvider.get(k, PersistentDataType.TAG_CONTAINER);
            if(provider == null) continue;
            EntityTickableModifier mod = parseModifier(k, provider, path);
            //provider nested in provider
            if(mod == null){
                List<NamespacedKey> newPath = path == null ? new ArrayList<>() : new ArrayList<>(path);
                newPath.add(k);
                list.addAll(parseModifiers(provider, newPath));
            } else{
                list.add(mod);
            }
        }
        return list;
    }

    private static EntityTickableModifier parseModifier(Key key, PersistentDataContainer data,
                                                        @Nullable List<NamespacedKey> path){
        Key groupKey = CruxTag.get(data, "slot", CruxPersistence.CRUX_KEY, null);
        if(groupKey == null) return null;
        return EntityTickableModifier.modifier(key, CruxAttributeRegistries.SLOT_GROUP.get(groupKey),
            (path == null || path.isEmpty()) ? null : path.toArray(new Key[0]));
    }

    private static <T extends PersistentDataContainer> PersistentDataContainer getSlotContainer(T base, CruxSlot slot){
        return CruxTag.get(base, slot.key(), PersistentDataType.TAG_CONTAINER, null);
    }

    private static <T extends PersistentDataContainer> PersistentDataContainer getModifierContainer(T base, EntityTickable tickable){
        return CruxTag.get(base, tickable.key(), PersistentDataType.TAG_CONTAINER, null);
    }

    private static <T extends PersistentDataContainer> PersistentDataContainer getTickablesContainer(T base){
        return CruxTag.get(base, "entity_tickables", PersistentDataType.TAG_CONTAINER, null);
    }

    private static <T extends PersistentDataContainer> PersistentDataContainer getComponents(T base){
        return CruxTag.get(base, "components", PersistentDataType.TAG_CONTAINER, null);
    }
    private static <T extends PersistentDataHolder> PersistentDataContainer getComponents(T base){
        return CruxTag.get(base, "components", PersistentDataType.TAG_CONTAINER, null);
    }
}
