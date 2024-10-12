package killercreepr.cruxblocks.block.standard.active;

import killercreepr.crux.data.tick.ManagedTicked;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import killercreepr.cruxblocks.block.standard.component.EntitySpawnerComponent;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class ActiveEntitySpawner extends ActiveCruxBlockImpl implements ManagedTicked {
    protected final @NotNull EntitySpawnerComponent data;
    protected final @NotNull NaturalEntitySpawner spawner;
    public ActiveEntitySpawner(@NotNull Block block, @NotNull CruxBlock cruxBlock, @NotNull EntitySpawnerComponent data, @NotNull NaturalEntitySpawner spawner) {
        super(block, cruxBlock);
        this.data = data;
        this.spawner = spawner;
    }

    protected int tick = 0;
    protected int delay = 0;
    @Override
    public void tick() {
        tick++;
        if(delay > 0){
            delay--;
            return;
        }
        delay = data.spawnDelay.value().intValue();
        spawner.navigate(block.getWorld(), CruxPosition.block(block));
    }
}
