package killercreepr.cruxtickables.core.entity.memory;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.TickedDataHolder;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.EntityTickedDataHolder;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.bukkit.AttributeBukkitAdaptor;
import killercreepr.cruxtickables.api.entity.tickable.ActiveEntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import killercreepr.cruxtickables.api.equipment.SetBonus;
import killercreepr.cruxtickables.api.event.EntityTickablesCalculateEvent;
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
import java.util.Objects;
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

    protected ActiveEntityTickable[] removeBuffer = new ActiveEntityTickable[16];
    protected int removeCount = 0;
    protected void bufferRemove(){
        for (int i = 0; i < removeCount; i++) {
            activeTickables.remove(removeBuffer[i].getTickable().key());
            removeBuffer[i] = null;
        }
        removeCount = 0;
    }

    protected void addRemove(ActiveEntityTickable holder) {
        if (removeCount == removeBuffer.length) {
            ActiveEntityTickable[] newBuf =
                new ActiveEntityTickable[removeBuffer.length * 2];
            System.arraycopy(removeBuffer, 0, newBuf, 0, removeBuffer.length);
            removeBuffer = newBuf;
        }
        removeBuffer[removeCount++] = holder;
    }

    public void updateTickables(Entity user){
        if(!(user instanceof LivingEntity e)) return;
        setActiveTickables(calculateTickables(e));
    }

    public void tickTickables(){
        removeCount = 0;
        for (ActiveEntityTickable value : activeTickables.values()) {
            if(value.shouldStop()){
                addRemove(value);
                continue;
            }
            value.tick();
        }
        bufferRemove();

        /*activeTickables.values().removeIf(active ->{
            if(active.shouldStop()){
                active.stopped();
                return true;
            }
            active.tick();
            return false;
        });*/
    }

    public ActiveEntityTickable getTickable(EntityTickable tickable){
        return getTickable(tickable.key());
    }

    public ActiveEntityTickable getTickable(Key key){
        return activeTickables.get(key);
    }

    public void setActiveTickables(Map<Key, ActiveEntityTickable> tickables){
        if(tickables == null || tickables.isEmpty()){
            activeTickables.values().forEach(ManagedTicked::stopped);
            activeTickables.clear();
            return;
        }
        activeTickables.values().removeIf(active ->{
            if(tickables.containsKey(active.getTickable().key())){
                if(Objects.equals(active.getSlot(), tickables.get(active.getTickable().key()).getSlot())) return false;
                //return false
            }
            active.stopped();
            return true;
        });
        tickables.forEach((key, active) ->{
            if(activeTickables.containsKey(key)) return;
            activeTickables.put(key, active);
            active.started();
        });
    }

    private void addTickablesFromItem(Map<Key, ActiveEntityTickable> map,
                                      Map<Key, TrackedBonus> setBonus,
                                      Entity user,
                                      ItemStack item,
                                      CruxSlot slot){
        if(CruxItem.isEmpty(item)) return;
        CruxItem crux = CruxItem.wrap(item);
        EntityTickablesContainer container = crux.getOrDefaultData(CruxTickableComponents.ENTITY_TICKABLES);
        if(container== null) return;
        container.getTickableModifiers().forEach(mod ->{
            if(!mod.getSlotGroup().test(slot)) return;

            SetBonus bonus = mod.getSetBonus();
            if(bonus != null){
                TrackedBonus tracked = setBonus.get(mod.getTickable().key());
                if(tracked == null){
                    tracked = new TrackedBonus(mod.getTickable());
                    setBonus.put(mod.getTickable().key(), tracked);
                }
                if(bonus.isMain()){
                    tracked.mainBonus = bonus;
                    tracked.modifier = mod;
                    tracked.slot = slot;
                }
                tracked.bonuses++;
                return;
            }

            ActiveEntityTickable active = mod.getTickable().buildActive(user, slot, mod);
            if(active==null) return;
            map.put(active.getTickable().key(), active);
        });
    }

    public @NotNull Map<Key, ActiveEntityTickable> calculateTickables(@NotNull LivingEntity e){
        EntityEquipment equipment = e.getEquipment();
        if(equipment==null) return Map.of();
        Map<Key, ActiveEntityTickable> map = new HashMap<>();
        Map<Key, TrackedBonus> setBonus = new HashMap<>();

        for(CruxSlot slot : CruxSlotGroup.ARMOR){
            EquipmentSlot equipmentSlot = AttributeBukkitAdaptor.adapt(slot);
            if(equipmentSlot==null) continue;
            addTickablesFromItem(map, setBonus, e, equipment.getItem(equipmentSlot), slot);
        }

        for(CruxSlot slot : CruxSlotGroup.HAND){
            EquipmentSlot equipmentSlot = AttributeBukkitAdaptor.adapt(slot);
            if(equipmentSlot==null) continue;
            addTickablesFromItem(map, setBonus, e, equipment.getItem(equipmentSlot), slot);
        }

        for (TrackedBonus tracked : setBonus.values()) {
            SetBonus main = tracked.mainBonus;
            if(main == null) continue;
            if(tracked.bonuses < main.getEquipmentAmount()) continue;
            ActiveEntityTickable active = tracked.tickable.buildActive(e, tracked.slot, tracked.modifier);
            if(active==null) continue;
            map.put(active.getTickable().key(), active);
        }

        EntityTickablesCalculateEvent event = new EntityTickablesCalculateEvent(e, map);
        event.callEvent();
        return map;
    }

    protected long lastItemUpdate;

    public long getLastItemUpdate() {
        return lastItemUpdate;
    }

    public void setLastItemUpdate(long lastItemUpdate) {
        this.lastItemUpdate = lastItemUpdate;
    }

    protected int tick = 0;
    @Override
    public void tick(@NotNull Entity e) {
        tickTickables();
        tick++;
        if(tick > 20){
            tick = 0;
            if(!CruxMath.hasOccurredWithin(lastItemUpdate, 100)) return;
            updateTickables(e);
        }
    }

    public static class TrackedBonus{
        public SetBonus mainBonus;
        public final EntityTickable tickable;
        public EntityTickableModifier modifier;
        public int bonuses;
        public CruxSlot slot;

        public TrackedBonus(EntityTickable tickable) {
            this.tickable = tickable;
        }
    }
}
