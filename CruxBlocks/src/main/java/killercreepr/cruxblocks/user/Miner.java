package killercreepr.cruxblocks.user;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an object that can manipulate (mine/place) blocks.
 */
public interface Miner {
    Object getHandle();
    static Miner block(@NotNull Block block){
        return new BlockMiner(block);
    }
    static Miner entity(@NotNull Entity entity){
        return new EntityMiner(entity);
    }

    static Miner entity(@Nullable ItemStack tool, @NotNull Entity entity){
        return new EntityMiner(tool, entity);
    }

    static Miner item(@Nullable ItemStack tool){
        return new ItemMiner(tool);
    }
}
