package killercreepr.crux.core.entity.consumer;

import killercreepr.crux.api.entity.consumer.CruxEntityConsumer;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxCollection;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OminousStandardEntityConsumer implements CruxEntityConsumer {
    protected final Key key;

    public OminousStandardEntityConsumer(Key key) {
        this.key = key;
    }

    @Override
    public void accept(@NotNull Entity e) {
        //    Chainmail armor with bolt armor trim in copper (4 in 7)
        //    Iron armor with flow armor trim in copper (2 in 7)
        //    Diamond armor with flow armor trim in copper (1 in 7).
        if(!(e instanceof LivingEntity living)) return;
        EntityEquipment equipment = living.getEquipment();
        if(equipment == null) return;

        LootContext ctx = LootContext.builder()
            .looter(e)
            .location(e.getLocation())
            .build();
        for(EquipmentSlot slot : EquipmentSlot.values()){
            if(!living.canUseEquipmentSlot(slot)) continue;
            ItemStack current = equipment.getItem(slot);
            if(!CruxItem.isEmpty(current)) continue;

            Key key = Crux.key("spawner/ominous/standard/" + slot.toString().toLowerCase());
            LootTable<ItemStack> lootTable = CruxRegistries.ITEM_LOOT_TABLE.get(key);
            if(lootTable == null) continue;
            ItemStack item = CruxCollection.getFirst(lootTable.populateLoot(ctx));
            if(CruxItem.isEmpty(item)) continue;

            equipment.setItem(slot, item, true);
            equipment.setDropChance(slot, 0f);
        }
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
