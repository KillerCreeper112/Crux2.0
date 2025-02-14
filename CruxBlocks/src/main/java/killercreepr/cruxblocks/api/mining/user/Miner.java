package killercreepr.cruxblocks.api.mining.user;

import killercreepr.cruxblocks.core.mining.user.BlockMiner;
import killercreepr.cruxblocks.core.mining.user.EntityMiner;
import killercreepr.cruxblocks.core.mining.user.ItemMiner;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
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
        return entity(entity, null);
    }

    static Miner entity(@Nullable ItemStack tool, @NotNull Entity entity){
        return entity(tool, entity, null);
    }

    static Miner entity(@NotNull Entity entity, @Nullable EquipmentSlot hand){
        return new EntityMiner(entity, hand);
    }

    static Miner entity(@Nullable ItemStack tool, @NotNull Entity entity, @Nullable EquipmentSlot hand){
        return new EntityMiner(tool, entity, hand);
    }

    static Miner item(@Nullable ItemStack tool){
        return new ItemMiner(tool);
    }
}
