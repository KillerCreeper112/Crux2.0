package killercreepr.crux.core.loot.conditions.entity;

import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EntityCondition extends BaseCondition {
    protected final @Nullable EntityPredicate entityPredicate;
    protected final @Nullable String worldName;
    protected final @Nullable Map<EquipmentSlot, LootCondition> slots;
    public EntityCondition(@NotNull String target, @Nullable EntityPredicate entityPredicate, @Nullable String worldName, @Nullable Map<EquipmentSlot, LootCondition> slots) {
        super(target);
        this.entityPredicate = entityPredicate;
        this.worldName = worldName;
        this.slots = slots;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Entity e = ctx.info().get(target, Entity.class);
        if(e==null) return false;
        if(entityPredicate != null && !entityPredicate.test(e)) return false;
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
