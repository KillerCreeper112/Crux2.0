package killercreepr.cruxtickables.core.entity.memory;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.EntityTickedDataHolder;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxtickables.api.entity.tickable.ActiveEntityTickable;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EntityTickablesHolder extends EntityTickedDataHolder {
    public static final Key KEY = Crux.key("tickables");
    public EntityTickablesHolder(@NotNull Key key, @NotNull EntityMemory parent) {
        super(key, parent);
    }
    public EntityTickablesHolder(@NotNull EntityMemory parent) {
        this(KEY, parent);
    }

    private void applyTickables(){

    }

    private Map<Key, ActiveEntityTickable> getTickables(CruxItem item, CruxSlot slot){

    }

    private @NotNull Map<Key, ActiveEntityTickable> calculateTickables(@NotNull LivingEntity e){
        Map<Key, ActiveEntityTickable> map = new HashMap<>();

        ItemStack hand = p.getInventory().getItemInMainHand();
        addDistinct(p, b, GrimSlot.HAND, Tickables.get(hand, GrimSlot.HAND));
        hand = p.getInventory().getItemInOffHand();
        addDistinct(p, b, GrimSlot.OFF_HAND, Tickables.get(hand, GrimSlot.OFF_HAND));

        for(GrimSlot slot : GrimSlot.ANY_ACCESSORY.getSlots()){
            ItemStack i = p.getInventory().getItem(slot.getIndex());
            addDistinct(p, b, slot, Tickables.get(i, slot));
            addDistinct(p, b, slot, Tickables.get(i, GrimSlot.ANY_ACCESSORY));
        }

        //armor bonuses
        Collection<Tickables> bonus = new HashSet<>();
        int setBonus = 0;
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(bonus.isEmpty()){
                bonus = Tickables.getArmorBonus(i);
                setBonus = 1;
            }else if (bonus.containsAll(Tickables.getArmorBonus(i))){
                setBonus++;
            }else{ setBonus = 0; break; }
        }
        if(bonus.isEmpty() || setBonus < 4) return b;
        return addDistinct(p, b, GrimSlot.CHEST, bonus);
    }

    @Override
    public void tick(@NotNull Entity e) {

    }
}
