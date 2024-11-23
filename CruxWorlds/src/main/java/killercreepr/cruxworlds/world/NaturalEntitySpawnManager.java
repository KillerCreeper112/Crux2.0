package killercreepr.cruxworlds.world;

import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxworlds.world.entity.NaturalEntityWorldSpawner;
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
    protected final @NotNull NaturalEntityWorldSpawner naturalEntitySpawner;

    protected int naturalSpawnTick = 0;
    protected final Collection<CruxPosition> recentlyCheckedMobSpawns = new HashSet<>();

    protected int lastMobAmount;
    protected long lastCheckedMobAmount;
    public NaturalEntitySpawnManager(@NotNull CruxWorld world, @NotNull NaturalEntityWorldSpawner naturalEntitySpawner) {
        this.world = world;
        this.naturalEntitySpawner = naturalEntitySpawner;
    }

    public @NotNull CruxWorld getWorld() {
        return world;
    }

    public int maxNaturalSpawnTick(){
        return CruxMath.random(100, 200);
    }

    public void naturalSpawnTick(){
        World world = this.world.toBukkitWorld();
        if(naturalSpawnTick < 0 || world.getPlayers().isEmpty()) return;
        naturalSpawnTick++;
        if(naturalSpawnTick < maxNaturalSpawnTick()) return;
        naturalSpawnTick = 0;
        naturalEntitySpawner.checkCanNavigate(world).whenComplete((value, throwable) ->{
            if(throwable !=  null) Crux.log(Level.WARNING, throwable.getMessage());
            if(!value) return;

            if(!onSpawnNavigation()) return;
            recentlyCheckedMobSpawns.clear();
            for(Player p : world.getPlayers()){
                if(p.getGameMode() == GameMode.SPECTATOR || nearChecked(p)) continue;
                if(!onSpawnNavigation(p)) continue;
                CruxPosition position = CruxPosition.block(p.getLocation());
                recentlyCheckedMobSpawns.add(position);
                naturalEntitySpawner.navigate(world, position, spawner -> p.isOnline() && p.isValid(), spawner ->{
                    naturalSpawnerChecked(p);
                });
            }
        });
    }

    /**
     * @return If this returns false, the spawn navigation will not commence for the player.
     */
    public boolean onSpawnNavigation(@NotNull Player p){
        return true;
    }
    /**
     * @return If this returns false, the spawn navigation will not commence.
     */
    public boolean onSpawnNavigation(){
        return true;
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

