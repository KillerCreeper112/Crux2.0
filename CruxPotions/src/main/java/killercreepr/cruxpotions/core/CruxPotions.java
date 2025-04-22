package killercreepr.cruxpotions.core;

import killercreepr.crux.core.util.CruxTag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CruxPotions {
    public static boolean canApplyPotion(@NotNull Entity e){
        if(CruxTag.has(e, "disable_potions") || !(e instanceof LivingEntity)) return false;
        return switch (e.getType()){
            case ARMOR_STAND, UNKNOWN-> false;
            default -> true;
        };
    }
}
