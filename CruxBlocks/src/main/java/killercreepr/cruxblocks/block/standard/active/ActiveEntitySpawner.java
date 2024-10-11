package killercreepr.cruxblocks.block.standard.active;

import killercreepr.crux.data.tick.ManagedTicked;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class ActiveEntitySpawner extends ActiveCruxBlockImpl implements ManagedTicked {
    public ActiveEntitySpawner(@NotNull Block block, @NotNull CruxBlock cruxBlock) {
        super(block, cruxBlock);
    }


    @Override
    public void tick() {

    }
}
