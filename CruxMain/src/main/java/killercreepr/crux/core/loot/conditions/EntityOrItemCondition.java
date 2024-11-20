package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class EntityOrItemCondition extends BaseCondition{
    protected final @NotNull LootCondition itemCondition;
    protected final @Nullable Collection<EquipmentSlot> ifEntitySlots;
    public EntityOrItemCondition(@NotNull String target, @NotNull LootCondition itemCondition, @Nullable Collection<EquipmentSlot> ifEntitySlots) {
        super(target);
        this.itemCondition = itemCondition;
        this.ifEntitySlots = ifEntitySlots;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object object = ctx.info().get(target);
        if(object instanceof LivingEntity e){
            EntityEquipment equipment = e.getEquipment();
            if(equipment==null) return false;
            Collection<EquipmentSlot> checkSlots = ifEntitySlots == null ? Set.of(EquipmentSlot.HAND) : ifEntitySlots;
            for(EquipmentSlot slot : checkSlots){
                LootContext itemCtx = ctx.withInfo(ctx.info().append("this", () -> equipment.getItem(slot)));
                if(itemCondition.test(itemCtx)) return true;
            }
            return false;
        }
        LootContext itemCtx = ctx.withInfo(ctx.info().append("this", Holder.direct(object)));
        return itemCondition.test(itemCtx);
    }
}
