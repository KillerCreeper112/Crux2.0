package killercreepr.cruxblocks.core.block.component.standard;

import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxTickedBlock;
import killercreepr.cruxblocks.core.block.data.CustomBlockData;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawner;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BlockEntitySpawner implements Ticked {
    protected final Block block;
    protected final EntitySpawnerComponent data;
    protected final NaturalEntitySpawner spawner;
    protected final Consumer<Entity> spawnConsumer;
    public BlockEntitySpawner(
        Block block, EntitySpawnerComponent data, @Nullable NaturalEntitySpawner spawner) {
        this.block = block;
        this.data = data;
        this.spawner = spawner;
        if(data == null || spawner == null){
            this.spawnConsumer = null;
            return;
        }
        this.spawnConsumer = data.persistEntities ? e ->{
            e.setPersistent(true);
            if(e instanceof Mob m) m.setRemoveWhenFarAway(false);
        } : null;
    }

    public BlockEntitySpawner(Block block, EntitySpawnerComponent data, NaturalEntitySpawner spawner, Consumer<Entity> spawnConsumer) {
        this.block = block;
        this.data = data;
        this.spawner = spawner;
        this.spawnConsumer = spawnConsumer;
    }

    protected CompletableFuture<List<Entity>> lastSpawned = null;
    protected final Runnable task = () -> {
        if(!isActive()){
            if(lastSpawned != null) lastSpawned.complete(null);
            failedNavigateSpawner();
            return;
        }
        navigateSpawner();
    };

    public void failedNavigateSpawner(){
        if(delay == -1){
            delay = data.failedDelay.value().intValue();
        }
    }

    public CompletableFuture<List<Entity>> navigateSpawner(){
        var future = spawner.navigate(block.getWorld(), CruxPosition.block(block), null, null, e ->{
            CruxPersist.SPAWN_REASON.set(e, "crux_spawner");
            onEntitySpawned(e);
        });
        if(future != null){
            if(lastSpawned != null){
                future.whenComplete((result, throwable) ->{
                    if(lastSpawned != null){
                        lastSpawned.complete(result);
                    }
                });
            }
        }
        return future;
    }

    public void onEntitySpawned(Entity e){

    }

    public void spawnerTick(){
        if(delay < 0) return;
        if(delay > 0){
            delay--;
            return;
        }
        delay = data.spawnDelay.value().intValue();
        lastSpawned = new CompletableFuture<>();
        Crux.scheduler().runTaskMain(task);
    }

    public CompletableFuture<List<Entity>> getLastSpawned() {
        return lastSpawned;
    }

    protected int delay = 0;
    @Override
    public void tick() {
        spawnerTick();
    }

    public boolean isActive(){
        double range = data.requiredPlayerRange.value().doubleValue();
        if(range == -1D) return true;

        if(data.ignoreCreativePlayers){
            return !block.getWorld().getNearbyEntitiesByType(Player.class, block.getLocation(), range, e -> switch (e.getGameMode()){
                case CREATIVE, SPECTATOR -> false;
                default -> true;
            }).isEmpty();
        }

        return !block.getWorld().getNearbyEntitiesByType(Player.class, block.getLocation(), range).isEmpty();
    }

    public @NotNull EntitySpawnerComponent getData() {
        return data;
    }

    public @NotNull NaturalEntitySpawner getSpawner() {
        return spawner;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
