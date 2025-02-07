package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public interface EntityTickable extends CruxKeyed {
    @Nullable ActiveEntityTickable buildActive(@NotNull Entity user, @Nullable CruxSlot slot);

    static <T extends PersistentDataContainer> EntityTickablesContainer getTickables(T base, CruxSlot slot){
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
    }

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

    @ApiStatus.Experimental
    static <P extends PersistentDataContainer> P addModifierDirect(@Nullable P base, @NotNull EntityTickableModifier modifier, @NotNull Key... path){
        if(base == null) return null;
        if(path == null || path.length==0) path = modifier.getPath();

        PersistentDataContainer modProvider = path == null || path.length < 1 ?
            CruxTag.get(base, modifier.key(), PersistentDataType.TAG_CONTAINER, null) :
            getModifierProvider(base, path);
        if(modProvider == null) modProvider = base.getAdapterContext().newPersistentDataContainer();
        if(!modifier.getSlotGroup().equals(CruxSlotGroup.ANY)){
            modProvider.set(Crux.key("slot"), CruxPersistence.CRUX_KEY, modifier.getSlotGroup().key());
        }

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

    private static EntityTickableModifier parseModifier(EntityTickable tickable, PersistentDataContainer data){
        Key groupKey = CruxTag.get(data, "slot", CruxPersistence.CRUX_KEY, null);
        return EntityTickableModifier.modifier(tickable, groupKey == null ? null : CruxAttributeRegistries.SLOT_GROUP.get(groupKey));
    }


    private static <T extends PersistentDataContainer> PersistentDataContainer getSlotContainer(T base, CruxSlot slot){
        return CruxTag.get(base, slot.key(), PersistentDataType.TAG_CONTAINER, null);
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
