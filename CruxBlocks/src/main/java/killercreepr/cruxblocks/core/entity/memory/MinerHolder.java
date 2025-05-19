package killercreepr.cruxblocks.core.entity.memory;

import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.CruxComponents;
import killercreepr.crux.core.entity.memory.PlayerTickedDataHolder;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.manager.CruxBlockManager;
import killercreepr.cruxblocks.api.mining.user.Miner;
import net.kyori.adventure.key.Key;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinerHolder extends PlayerTickedDataHolder {
    public static final Key KEY = Crux.key("miner");
    protected final @NotNull CruxBlockManager blockManager;
    public MinerHolder(@NotNull Key key, @NotNull PlayerMemory parent, @NotNull CruxBlockManager blockManager) {
        super(key, parent);
        this.blockManager = blockManager;
    }

    public MinerHolder(@NotNull PlayerMemory parent, @NotNull CruxBlockManager blockManager) {
        this(KEY, parent, blockManager);
    }

    //protected @Nullable Double lastBreakSpeed;
    protected @Nullable Byte lastMine;
    @Override
    public void onTick(@NotNull Player e) {
        if(lastMine == null) return;
        lastMine--;
        if(lastMine > 0) return;
        //if(hasMinedWithin(3)) return;
        lastMine = null;
        resetBreakSpeed(e);
    }

    @Override
    protected void removingFromMemory(@Nullable Entity e) {
        super.removingFromMemory(e);
        if(!(e instanceof Player p)) return;
        resetBreakSpeed(p);
    }

    public void resetBreakSpeed(@NotNull Player p){
        /*if(lastBreakSpeed != null){
            p.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(lastBreakSpeed);
            lastBreakSpeed = null;
        }*/
        p.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(1D);
    }

    public int getBlockInteractionRange(@NotNull Player p){
        return (int) p.getAttribute(Attribute.BLOCK_INTERACTION_RANGE).getValue();
    }

    public void onMine(@NotNull Player p){
        Block block = p.getTargetBlockExact(getBlockInteractionRange(p));
        onMine(p, block);
    }

    public void onMine(@NotNull Player p, @Nullable Block block){
        lastMine = null;
        resetBreakSpeed(p);
        if(block == null) return;
        ActiveCruxBlock active = blockManager.getActiveBlock(block);
        if(active== null) return;
        onMine(p, active);
    }

    public void onMine(@NotNull Player p, @NotNull ActiveCruxBlock block){
        lastMine = 2;
        float mineSpeed = block.getCruxBlock().getComponents().getOrDefault(CruxComponents.UNBREAKABLE, false) ?
            0f : block.getMineSpeed(Miner.entity(p.getInventory().getItemInMainHand(), p), true);
        /*if(lastBreakSpeed == null){
            lastBreakSpeed = p.getAttribute(Attribute.BLOCK_BREAK_SPEED).getBaseValue();
        }*/
        setMineSpeed(p, mineSpeed);
    }

    public void setMineSpeed(@NotNull Player p, double mineSpeed){
        //mineSpeed = mineSpeed <= 0D ? 0D : 1D + mineSpeed;
        //if(mineSpeed < 0D) mineSpeed = Double.MAX_VALUE;
        p.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(mineSpeed);
    }

    /*public boolean hasMinedWithin(int ticks){
        if(lastMine == null) return false;
        return CruxMath.hasOccurredWithin(lastMine, ticks);
    }*/

    /*public @Nullable Long getLastMine() {
        return lastMine;
    }*/

    public @NotNull CruxBlockManager getBlockManager() {
        return blockManager;
    }

    /*public @Nullable Double getLastBreakSpeed() {
        return lastBreakSpeed;
    }

    public void setLastBreakSpeed(@Nullable Double lastBreakSpeed) {
        this.lastBreakSpeed = lastBreakSpeed;
    }*/

    public void setLastMine(@Nullable Byte lastMine) {
        this.lastMine = lastMine;
    }
}
