package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxCollection;
import killercreepr.cruxworlds.api.component.EntitySpawnComponent;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EntitySpawnEquipment implements EntitySpawnComponent {
    public final Map<Object, LootTable<ItemStack>> equipment;

    public EntitySpawnEquipment(Map<Object, LootTable<ItemStack>> equipment) {
        this.equipment = equipment;
    }

    @Override
    public void onCreateEntity(@NotNull SpawnContext ctx, Entity e, TextParserContext textCtx) {
        if(!(e instanceof LivingEntity living)) return;
        EntityEquipment equip = living.getEquipment();
        if(equip == null) return;

        LootContext lootCtx = LootContext.builder()
            .looted(e)
            .looter(ctx)
            .random(ctx.getRandom())
            .build();

        equipment.forEach((keyObject, table) ->{
            EquipmentSlot slot;
            try{
                slot = EquipmentSlot.valueOf(textCtx.deserializeString(keyObject.toString()).toUpperCase());
            } catch (IllegalArgumentException ex) {
                return;
            }
            if(!living.canUseEquipmentSlot(slot)) return;

            ItemStack item = CruxCollection.getFirst(table.populateLoot(lootCtx));
            if(item == null) return;

            equip.setItem(slot, item);
        });
    }
}
