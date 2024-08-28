package killercreepr.crux.loot.impl.conditions.entity;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.impl.conditions.BaseCondition;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EntityCondition extends BaseCondition {
    protected final @Nullable Key entityType;
    protected final @Nullable String worldName;
    protected final @Nullable Map<EquipmentSlot, LootCondition> slots;
    public EntityCondition(@NotNull String target, @Nullable Key entityType, @Nullable String worldName, @Nullable Map<EquipmentSlot, LootCondition> slots) {
        super(target);
        this.entityType = entityType;
        this.worldName = worldName;
        this.slots = slots;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Entity e = ctx.info().get(target, Entity.class);
        if(e==null) return false;
        if(entityType != null && !e.getType().key().equals(entityType)) return false;
        if(worldName != null && !e.getWorld().getName().equals(worldName)) return false;
        if(slots != null){
            EntityEquipment equipment = e instanceof LivingEntity dd ? dd.getEquipment() : null;
            for(Map.Entry<EquipmentSlot, LootCondition> entry : slots.entrySet()){
                EquipmentSlot slot = entry.getKey();
                ItemStack item = equipment == null ? null : equipment.getItem(slot);
                if(!entry.getValue().test(ctx.withInfo(ctx.info().append("this", () -> item)))) return false;
            }
        }
        return true;
    }
}
