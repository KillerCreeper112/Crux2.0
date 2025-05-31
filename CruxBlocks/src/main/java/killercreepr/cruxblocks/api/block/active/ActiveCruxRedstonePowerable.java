package killercreepr.cruxblocks.api.block.active;

import org.bukkit.block.Block;

public interface ActiveCruxRedstonePowerable {
    default boolean isRedstonePowerable(){
        return true;
    }
    void redstonePowerChanged(Block from, int newPower);
}
