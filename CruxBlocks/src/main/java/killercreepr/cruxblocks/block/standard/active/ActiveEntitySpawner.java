package killercreepr.cruxblocks.block.standard.active;

import killercreepr.crux.Crux;
import killercreepr.crux.data.tick.ManagedTicked;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import killercreepr.cruxblocks.block.data.CustomBlockData;
import killercreepr.cruxblocks.block.standard.component.EntitySpawnerComponent;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ActiveEntitySpawner extends ActiveCruxBlockImpl implements ManagedTicked {
    protected final @NotNull EntitySpawnerComponent data;
    protected final @NotNull NaturalEntitySpawner spawner;
    public ActiveEntitySpawner(@NotNull Block block, @NotNull CruxBlock cruxBlock, @NotNull EntitySpawnerComponent data, @NotNull NaturalEntitySpawner spawner) {
        super(block, cruxBlock);
        this.data = data;
        this.spawner = spawner;
    }

    @Override
    public void started() {
        CustomBlockData data = CustomBlockData.wrap(block);
        delay = data.get("delay", PersistentDataType.INTEGER, 0);
    }

    @Override
    public void stopped() {
        CustomBlockData data = CustomBlockData.wrap(block);
        data.set("delay", PersistentDataType.INTEGER, delay);
    }

    protected final Runnable task = () -> {
        if(!isActive()) return;
        navigateSpawner();
    };

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
