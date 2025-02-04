package killercreepr.cruxblocks.core.block.active.standard;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxTickedBlock;
import killercreepr.cruxblocks.core.block.active.SimpleActiveCruxBlock;
import killercreepr.cruxblocks.core.block.component.standard.EntitySpawnerComponent;
import killercreepr.cruxblocks.core.block.data.CustomBlockData;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawner;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ActiveEntitySpawner extends SimpleActiveCruxBlock implements ActiveCruxTickedBlock {
    protected final @NotNull EntitySpawnerComponent data;
    protected final @NotNull NaturalEntitySpawner spawner;
    protected final Consumer<Entity> spawnConsumer;
    public ActiveEntitySpawner(@NotNull Block block, @NotNull CruxBlock cruxBlock, @NotNull EntitySpawnerComponent data, @NotNull NaturalEntitySpawner spawner) {
        super(block, cruxBlock);
        this.data = data;
        this.spawner = spawner;
        this.spawnConsumer = data.persistEntities ? e ->{
            e.setPersistent(true);
            if(e instanceof Mob m) m.setRemoveWhenFarAway(false);
        } : null;
    }

    @Override
    public void started() {
        ActiveCruxTickedBlock.super.started();
        CustomBlockData data = CustomBlockData.wrap(block);
        delay = data.get("delay", PersistentDataType.INTEGER, 0);
    }

    @Override
    public void stopped() {
        ActiveCruxTickedBlock.super.stopped();
        CustomBlockData data = CustomBlockData.wrap(block);
        data.set("delay", PersistentDataType.INTEGER, delay);
    }

    protected final Runnable task = () -> {
        if(!isActive()){
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

    public void navigateSpawner(){
        spawner.navigate(block.getWorld(), CruxPosition.block(block));
    }

    protected int delay = 0;
    @Override
    public void tick() {
        if(delay < 0) return;
        if(delay > 0){
            delay--;
            return;
        }
        delay = data.spawnDelay.value().intValue();
        Crux.getServer().getScheduler().runTask(Crux.getMainPlugin(), task);
    }

    public boolean isActive(){
        double range = data.requiredPlayerRange.value().doubleValue();

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
