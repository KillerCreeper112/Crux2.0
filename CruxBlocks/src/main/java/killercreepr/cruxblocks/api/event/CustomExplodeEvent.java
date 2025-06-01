package killercreepr.cruxblocks.api.event;

import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

/**
 * This event is used for the Custom entity and block events because when custom blocks are exploded
 * normally, they drop their normal item counterparts so we're using a wrapper event to fix it ._.
 */
public class CustomExplodeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Location location;
    private final List<Block> blocks;
    private final List<Block> wrappedBlocks;
    private float yield;
    private final ExplosionResult result;

    public CustomExplodeEvent(Location location, List<Block> blocks, List<Block> wrappedBlocks, ExplosionResult result, float yield) {
        this.location = location;
        this.blocks = blocks;
        this.wrappedBlocks = wrappedBlocks;
        this.result = result;
        this.yield = yield;
    }

    public List<Block> blockList(){
        return blocks;
    }

    public void addBlock(Block block){
        if(!blocks.contains(block)) blocks.add(block);
        if(!wrappedBlocks.contains(block)) wrappedBlocks.add(block);
    }

    public void clearBlocks(){
        blocks.clear();
        wrappedBlocks.clear();
    }

    public boolean removeBlockIf(Predicate<Block> filter){
        return blocks.removeIf(b ->{
            if(filter.test(b)){
                wrappedBlocks.remove(b);
                return true;
            }
            return false;
        });
    }

    public Location getLocation() {
        return location;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public float getYield() {
        return yield;
    }

    public void setYield(float yield) {
        this.yield = yield;
    }

    public ExplosionResult getResult() {
        return result;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList(){
        return handlers;
    }
}
