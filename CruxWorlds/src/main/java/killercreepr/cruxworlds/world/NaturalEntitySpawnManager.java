package killercreepr.cruxworlds.world;

import killercreepr.crux.Crux;
import killercreepr.crux.data.tick.Ticked;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxworlds.world.entity.entity.NaturalEntitySpawner;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;

public class NaturalEntitySpawnManager implements Ticked, Listener {
    protected final @NotNull CruxWorld world;
    protected final @NotNull NaturalEntitySpawner naturalEntitySpawner;

    protected int naturalSpawnTick = 0;
    protected final Collection<CruxPosition> recentlyCheckedMobSpawns = new HashSet<>();

    protected int lastMobAmount;
    protected long lastCheckedMobAmount;
    public NaturalEntitySpawnManager(@NotNull CruxWorld world, @NotNull NaturalEntitySpawner naturalEntitySpawner) {
        this.world = world;
        this.naturalEntitySpawner = naturalEntitySpawner;
    }

    public @NotNull CruxWorld getWorld() {
        return world;
    }

    public void naturalSpawnTick(){
        World world = this.world.toBukkitWorld();
        if(naturalSpawnTick < 0 || world.getPlayers().isEmpty()) return;
        naturalSpawnTick++;
        if(naturalSpawnTick < CruxMath.random(100, 200)) return;
        naturalSpawnTick = 0;
        naturalEntitySpawner.checkCanNavigate(world).whenComplete((value, throwable) ->{
            if(throwable !=  null) Crux.log(Level.WARNING, throwable.getMessage());
            if(!value) return;

            Crux.log(Level.INFO, "Navigating natural entity spawns...");
            recentlyCheckedMobSpawns.clear();
            for(Player p : world.getPlayers()){
                if(p.getGameMode() == GameMode.SPECTATOR || nearChecked(p)) continue;
                CruxPosition position = CruxPosition.block(p.getLocation());
                recentlyCheckedMobSpawns.add(position);
                naturalEntitySpawner.navigate(world, position, spawner -> p.isOnline() && p.isValid(), spawner ->{
                    naturalSpawnerChecked(p);
                });
            }
        });
    }

    @Override
    public void tick(){
        naturalSpawnTick();
    }

    public void naturalSpawnerChecked(@NotNull Player p){
        naturalSpawnTick = 0;
    }

    private boolean nearChecked(@NotNull Player p){
        Location x = p.getLocation();
        double radius = naturalEntitySpawner.getRadius() * .6D;
        for(CruxPosition b : recentlyCheckedMobSpawns){
            if(b.distanceSquared(CruxPosition.block(x)) < (radius*radius)) return true;
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if(!e.getWorld().equals(world.toBukkitWorld())) return;
        if(System.currentTimeMillis() > lastCheckedMobAmount){
            lastCheckedMobAmount = System.currentTimeMillis() + (50L*20);
            lastMobAmount = naturalEntitySpawner.getNaturallySpawnedMobs(world.toBukkitWorld());
        }
        if(!naturalEntitySpawner.isBelowGlobalMobLimit(lastMobAmount)){
            event.setCancelled(true);
        }
    }
}

