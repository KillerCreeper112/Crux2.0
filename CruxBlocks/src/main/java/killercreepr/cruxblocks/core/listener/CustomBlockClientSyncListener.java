package killercreepr.cruxblocks.core.listener;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.registry.CruxBlockRegistry;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CustomBlockClientSyncListener implements Listener {
    protected final @NotNull Plugin plugin;
    protected final @NotNull CruxBlockRegistry blockRegistry;
    public CustomBlockClientSyncListener(@NotNull Plugin plugin, @NotNull CruxBlockRegistry blockRegistry) {
        this.plugin = plugin;
        this.blockRegistry = blockRegistry;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent event) {
        plugin.getServer().getScheduler().runTaskLater(plugin, task ->{
            event.blockList().forEach(this::updateAllVertical);
        }, 2L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockExplode(BlockExplodeEvent event) {
        plugin.getServer().getScheduler().runTaskLater(plugin, task ->{
            event.blockList().forEach(this::updateAllVertical);
        }, 2L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        BlockFace direction = event.getDirection();
        Block b = event.getBlock();
        update(b, direction, event.getBlocks(), false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        BlockFace direction = event.getDirection();
        Block b = event.getBlock();
        update(b, direction, event.getBlocks(), true);
    }

    public void update(@NotNull Block b, @NotNull BlockFace direction, @NotNull Collection<Block> blocks, boolean retract){
        CruxBlock up = blockRegistry.getByBlock(b.getRelative(BlockFace.UP));
        CruxBlock down = blockRegistry.getByBlock(b.getRelative(BlockFace.DOWN));
        if(up == null && down == null) return;

        plugin.getServer().getScheduler().runTaskLater(plugin, task ->{
            updateAllVertical(b);
            updateAllVertical(b.getRelative(direction));
            for(Block bb : blocks){
                updateAllVertical(bb.getRelative(direction));
                if(retract) updateAllVertical(bb);
            }
        }, 2L);
    }

    public void update(@NotNull Block block, @NotNull BlockFace direction){
        World world = block.getWorld();
        world.getPlayers().forEach(p ->{
            p.sendBlockChange(block.getLocation(), block.getBlockData());
        });
        Block b = block.getRelative(direction);
        if(blockRegistry.getByBlock(b) != null) update(b, direction);
    }

    public void updateAllVertical(@NotNull Block b){
        update(b, BlockFace.UP);
        update(b.getRelative(BlockFace.DOWN), BlockFace.DOWN);
    }
}
