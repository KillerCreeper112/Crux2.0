package killercreepr.cruxentities.listener;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.SimpleBlockComponentWrapper;
import killercreepr.cruxentities.component.CruxEntityComponents;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ComponentsListener implements Listener {
    @EventHandler(ignoreCancelled = true)
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
        Key customKey = spawnerData.getFromVanillaToCustom().get(e.getType().key());
        if(customKey == null) return;
        CruxMob mob = CruxEntityRegistries.ENTITIES.get(customKey);
        if(mob == null){
            Crux.logError("Trial spawner at " + b + " tried spawning an invalid CruxMob! " + customKey);
            return;
        }
        e.remove();
        mob.spawn(e.getLocation());
    }

}
