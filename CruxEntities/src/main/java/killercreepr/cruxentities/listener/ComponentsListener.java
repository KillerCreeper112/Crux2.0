package killercreepr.cruxentities.listener;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.SimpleBlockComponentWrapper;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.cruxentities.component.CruxEntityComponents;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ComponentsListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        CruxItem cruxItem = CruxItem.wrap(item);

        var spawnerData = cruxItem.get(CruxEntityComponents.CREATURE_SPAWNER_DATA);
        if(spawnerData == null) return;

        var components = new SimpleBlockComponentWrapper(b.getState());
        components.set(CruxEntityComponents.CREATURE_SPAWNER_DATA, spawnerData);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTrialSpawnerSpawn(TrialSpawnerSpawnEvent event) {
        Block b;
        try{
            b = event.getTrialSpawner().getBlock();
        }catch (IllegalStateException ignored){ return; }

        var components = new SimpleBlockComponentWrapper(b.getState());
        var spawnerData = components.get(CruxEntityComponents.CREATURE_SPAWNER_DATA);
        if(spawnerData == null) return;

        Entity e = event.getEntity();
        var mobData = spawnerData.getFromVanillaToCustom().get(e.getType().key());
        if(mobData == null) return;
        CruxMob mob = CruxEntityRegistries.ENTITIES.get(mobData.getMobType());
        if(mob == null){
            Crux.logError("Trial spawner at " + b + " tried spawning an invalid CruxMob! " + mobData.getMobType());
            return;
        }
        e.remove();
        Entity newEntity = mob.spawn(e.getLocation(), spawned ->{
            CruxPersist.SPAWN_REASON.set(spawned, "trial_spawner");

            if(mobData.isApplyEquipment() && spawned instanceof LivingEntity spawnedLiving && e instanceof LivingEntity living){
                var spawnedEquipment = spawnedLiving.getEquipment();
                var equipment = living.getEquipment();
                if(equipment != null && spawnedEquipment != null){
                    for(EquipmentSlot slot : EquipmentSlot.values()){
                        if(!living.canUseEquipmentSlot(slot) || !spawnedLiving.canUseEquipmentSlot(slot)) continue;
                        ItemStack current = spawnedEquipment.getItem(slot);
                        if(!CruxItem.isEmpty(current)) continue;
                        ItemStack newItem = equipment.getItem(slot);
                        if(CruxItem.isEmpty(newItem)) continue;

                        spawnedEquipment.setItem(slot, newItem, true);
                    }
                }
            }

        });

        final var newBlock = ((CraftBlockState) b.getState()).getWorldHandle().getBlockEntity(new BlockPos(b.getX(), b.getY(), b.getZ()));
        TrialSpawnerBlockEntity state = ((TrialSpawnerBlockEntity) newBlock);
        state.getTrialSpawner().getData().currentMobs.add(newEntity.getUniqueId());
    }

}
