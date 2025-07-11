package killercreepr.cruxentities.listener;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.entity.consumer.CruxEntityConsumer;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.SimpleBlockComponentWrapper;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxentities.component.CreatureSpawnerCfg;
import killercreepr.cruxentities.component.CruxEntityComponents;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
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

import java.util.Collection;

public class ComponentsListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        CruxItem cruxItem = CruxItem.wrap(item);


        CreatureSpawnerCfg cfg = cruxItem.get(CruxEntityComponents.CREATURE_SPAWNER_CONFIG);
        if(cfg != null){
            var components = new SimpleBlockComponentWrapper(b.getState());
            components.set(CruxEntityComponents.CREATURE_SPAWNER_CONFIG, cfg);
        }
        cfg = cruxItem.get(CruxEntityComponents.OMINOUS_CREATURE_SPAWNER_CONFIG);
        if(cfg != null){
            var components = new SimpleBlockComponentWrapper(b.getState());
            components.set(CruxEntityComponents.OMINOUS_CREATURE_SPAWNER_CONFIG, cfg);
        }

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

        final var newBlock = ((CraftBlockState) b.getState()).getWorldHandle().getBlockEntity(new BlockPos(b.getX(), b.getY(), b.getZ()));
        TrialSpawnerBlockEntity state = ((TrialSpawnerBlockEntity) newBlock);
        boolean ominous = state.getTrialSpawner().isOminous();

        Entity e = event.getEntity();
        var components = new SimpleBlockComponentWrapper(b.getState());
        CreatureSpawnerCfg cfg =
            components.get(ominous ? CruxEntityComponents.OMINOUS_CREATURE_SPAWNER_CONFIG : CruxEntityComponents.CREATURE_SPAWNER_CONFIG);
        if(cfg != null){
            LootContext lootCtx = LootContext.builder()
                .looted(b)
                .location(b.getLocation())
                .looter(event)
                .build();
            SpawnContext spawnCtx = SpawnContext.simple(
                e.getWorld(), CruxPosition.precise(e.getLocation()),
                CruxMath.random()
            );


            e.remove();
            cfg.getSpawns().populateLoot(lootCtx).forEach(group ->{
                if(!group.canSpawn(spawnCtx)) return;
                group.selectRandom(spawnCtx).forEach(spawn ->{
                    DataExchange info = spawn.info();
                    Entity newEntity = spawn.spawn(spawnCtx, spawned ->{
                        CruxPersist.SPAWN_REASON.set(spawned, "trial_spawner");

                        if(info.getOrDefault("inherit_equipment", Boolean.class, false)){
                            if(spawned instanceof LivingEntity spawnedLiving && e instanceof LivingEntity living){
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
                        }
                        if(ominous){
                            if(info.get("ominous_states") instanceof Collection<?> list){
                                list.forEach(stateKey ->{
                                    CruxEntityConsumer consumer = CruxRegistries.ENTITY_CONSUMER.get(
                                        Crux.key(stateKey.toString())
                                    );
                                    if(consumer == null) throw new IllegalArgumentException("CruxEntityConsumer of " + stateKey + " not found!");
                                    consumer.accept(spawned);
                                });
                            }
                        }
                    });
                    if(newEntity != null){
                        state.getTrialSpawner().getStateData().currentMobs.add(newEntity.getUniqueId());
                    }
                });
            });
            return;
        }

        var spawnerData = components.get(CruxEntityComponents.CREATURE_SPAWNER_DATA);
        if(spawnerData == null) return;

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

        /*final var newBlock = ((CraftBlockState) b.getState()).getWorldHandle().getBlockEntity(new BlockPos(b.getX(), b.getY(), b.getZ()));
        TrialSpawnerBlockEntity state = ((TrialSpawnerBlockEntity) newBlock);*/
        state.getTrialSpawner().getStateData().currentMobs.add(newEntity.getUniqueId());
    }

}
