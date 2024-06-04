package killercreepr.crux.attribute;

import killercreepr.crux.util.CruxString;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum CruxSlot {
    HAND(0),
    OFF_HAND(40),
    HEAD(39),
    CHEST(38),
    LEGS(37),
    FEET(36),
    ;

    public static final EquipmentSlot[] NORMAL_ARMOR = new EquipmentSlot[]{
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public static final CruxSlot[] ARMOR = new CruxSlot[]{
        CruxSlot.HEAD, CruxSlot.CHEST, CruxSlot.LEGS, CruxSlot.FEET
    };

    public static @Nullable CruxSlot adapt(@NotNull EquipmentSlot slot){
        return switch (slot){
            case FEET -> FEET;
            case LEGS -> LEGS;
            case CHEST -> CHEST;
            case HEAD -> HEAD;
            case HAND -> HAND;
            case OFF_HAND -> OFF_HAND;
            case BODY -> null;
        };
    }

    public @NotNull String getName(){
        return switch (this){
            case HAND -> "Main Hand";
            default -> CruxString.toTitleCase(this.toString());
        };
    }

    private final int index;
    private final Set<CruxSlot> slots;
    CruxSlot(int index){
        this.index = index;
        this.slots = null;
    }

    CruxSlot(int index, @NotNull CruxSlot @NotNull... slots){
        this.index = index;
        this.slots = new HashSet<>(Arrays.stream(slots).toList());
    }

    public Set<CruxSlot> getSlots() {
        return slots;
    }

    public static @Nullable CruxSlot getSlot(int index){
        for(CruxSlot s : values()){
            if(s.getIndex() == index) return s;
        }
        return null;
    }

    public static @Nullable CruxSlot getActivationSlot(@NotNull Player p, int slot){
        for(CruxSlot s : values()){
            if(s.getSlots() != null) continue;
            if(s.wouldActivate(p, slot)) return s;
        }
        return null;
    }

    public boolean wouldActivate(@NotNull Player p, int slot){
        if(getSlots() != null){
            for(CruxSlot s : getSlots()){
                if(s.wouldActivate(p, slot)) return true;
            }
            return false;
        }
        return switch (this){
            case HAND -> p.getInventory().getHeldItemSlot() == slot;
            default -> slot == index;
        };
    }

    public int getIndex(@NotNull Player p) {
        return switch (this){
            case HAND -> p.getInventory().getHeldItemSlot();
            default -> index;
        };
    }

    public int getIndex() {return index; }
    public @NotNull String getWhen(){
        return switch (this){
            case HEAD, CHEST, LEGS, FEET -> "When on " + getName();
            default -> "When in " + getName();
        };
    }

    public @NotNull NamespacedKey key(){
        return CruxAttribute.k("slot." + this.toString().toLowerCase());
    }

    public static @NotNull Map<@NotNull CruxSlot, @Nullable ItemStack> getItems(@NotNull Player p){
        Map<CruxSlot, ItemStack> map = new HashMap<>();
        for(CruxSlot s : values()){
            if(s.getSlots() != null) continue;
            map.put(s, s.getItem(p));
        }
        return map;
    }

    public @Nullable ItemStack getItem(@NotNull Player p){
        switch (this){
            case HAND ->{ return p.getInventory().getItemInMainHand(); }
            case OFF_HAND ->{ return p.getInventory().getItemInOffHand(); }
            default -> {
                try{
                    return p.getInventory().getItem(index);
                }catch (IllegalArgumentException e){ return null; }
            }
        }
    }
}
