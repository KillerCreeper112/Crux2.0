package killercreepr.cruxtickables.core.entity.memory;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.EntityTickedDataHolder;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.bukkit.AttributeBukkitAdaptor;
import killercreepr.cruxtickables.api.entity.tickable.ActiveEntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import killercreepr.cruxtickables.core.component.CruxTickableComponents;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityTickablesHolder extends EntityTickedDataHolder {
    public static final Key KEY = Crux.key("tickables");
    public EntityTickablesHolder(@NotNull Key key, @NotNull EntityMemory parent) {
        super(key, parent);
    }
    public EntityTickablesHolder(@NotNull EntityMemory parent) {
        this(KEY, parent);
    }
    protected final Map<Key, ActiveEntityTickable> activeTickables = new ConcurrentHashMap<>();

    public void updateTickables(Entity user){
        if(!(user instanceof LivingEntity e)) return;
        setActiveTickables(calculateTickables(e));
    }

    public void tickTickables(){
        activeTickables.values().removeIf(active ->{
            if(active.shouldStop()){
                active.stopped();
                return true;
            }
            active.tick();
            return false;
        });
    }

    public void setActiveTickables(Map<Key, ActiveEntityTickable> tickables){
        if(tickables == null || tickables.isEmpty()){
            activeTickables.values().forEach(ManagedTicked::stopped);
            activeTickables.clear();
            return;
        }
        activeTickables.values().removeIf(active ->{
            if(tickables.containsKey(active.getTickable().key())) return false;
            active.stopped();
            return true;
        });
        tickables.forEach((key, active) ->{
            activeTickables.put(key, active);
            active.started();
        });
    }

    private void addTickablesFromItem(Map<Key, ActiveEntityTickable> map, Entity user, ItemStack item, CruxSlot slot){
        if(CruxItem.isEmpty(item)) return;
        CruxItem crux = CruxItem.wrap(item);
        EntityTickablesContainer container = crux.get(CruxTickableComponents.ENTITY_TICKABLES);
        if(container== null) return;
        container.getTickableModifiers().forEach(mod ->{
            if(!mod.getSlotGroup().test(slot)) return;
            ActiveEntityTickable active = mod.getTickable().buildActive(user, slot, mod);
            if(active==null) return;
            map.put(active.getTickable().key(), active);
        });
    }

    public @NotNull Map<Key, ActiveEntityTickable> calculateTickables(@NotNull LivingEntity e){
        EntityEquipment equipment = e.getEquipment();
        if(equipment==null) return Map.of();
        Map<Key, ActiveEntityTickable> map = new HashMap<>();

        for(CruxSlot slot : CruxSlotGroup.ARMOR){
            EquipmentSlot equipmentSlot = AttributeBukkitAdaptor.adapt(slot);
            if(equipmentSlot==null) continue;
            addTickablesFromItem(map, e, equipment.getItem(equipmentSlot), slot);
        }

        for(CruxSlot slot : CruxSlotGroup.HAND){
            EquipmentSlot equipmentSlot = AttributeBukkitAdaptor.adapt(slot);
            if(equipmentSlot==null) continue;
            addTickablesFromItem(map, e, equipment.getItem(equipmentSlot), slot);
        }
        return map;
    }

    @Override
    public void tick(@NotNull Entity e) {
        tickTickables();
    }
}
