package killercreepr.cruxentities.listener;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxentities.entity.CruxMob;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class EntityManager implements Listener {
    protected final Plugin plugin;

    public EntityManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, task ->{
            for(Entity e : event.getEntities()){
                if(!e.isValid()) continue;
                loadEntity(e);
            }
        });
    }

    public static void loadEntity(@NotNull Entity e){
        if(e.getType() == EntityType.UNKNOWN) return;
        CruxMob mob = CruxMob.get(e);
        if(mob==null) return;
        mob.load(e);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        CruxMob mob = CruxMob.get(e);
        if(mob==null){
            Key key = Crux.handlers().entity().getType(e);
            Key lootKey = Crux.key("entity/" + key.value());

            LootTable<ItemStack> lootTable = CruxRegistries.ITEM_LOOT_TABLE.get(lootKey);
            if(lootTable == null) return;
            LootContext ctx = EventLootContexts.builder(event).build();
            event.getDrops().addAll(lootTable.populateLoot(ctx));
            return;
        }
        mob.onDeath(e, event);
    }
}
