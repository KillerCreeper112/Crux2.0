package killercreepr.cruxblocks.data.entity;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.crux.data.entity.PlayerTickedDataHolder;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.manager.CruxBlockManager;
import killercreepr.cruxblocks.user.Miner;
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

    protected @Nullable Double lastBreakSpeed;
    protected @Nullable Long lastMine;
    @Override
    public void onTick(@NotNull Player e) {
        if(lastMine == null) return;
        if(hasMinedWithin(2)) return;
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
        if(lastBreakSpeed != null){
            p.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).setBaseValue(lastBreakSpeed);
            lastBreakSpeed = null;
        }
    }

    public int getBlockInteractionRange(@NotNull Player p){
        return (int) p.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).getValue();
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
        lastMine = System.currentTimeMillis();
        float mineSpeed = block.getMineSpeed(Miner.entity(p.getInventory().getItemInMainHand(), p), true);
        if(lastBreakSpeed == null){
            lastBreakSpeed = p.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).getBaseValue();
        }
        setMineSpeed(p, mineSpeed);
    }

    public void setMineSpeed(@NotNull Player p, double mineSpeed){
        //mineSpeed = mineSpeed <= 0D ? 0D : 1D + mineSpeed;
        //if(mineSpeed < 0D) mineSpeed = Double.MAX_VALUE;
        p.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).setBaseValue(mineSpeed);
    }

    public boolean hasMinedWithin(int ticks){
        if(lastMine == null) return false;
        return CruxMath.hasOccurredWithin(lastMine, ticks);
    }

    public @Nullable Long getLastMine() {
        return lastMine;
    }

    public @NotNull CruxBlockManager getBlockManager() {
        return blockManager;
    }

    public @Nullable Double getLastBreakSpeed() {
        return lastBreakSpeed;
    }

    public void setLastBreakSpeed(@Nullable Double lastBreakSpeed) {
        this.lastBreakSpeed = lastBreakSpeed;
    }

    public void setLastMine(@Nullable Long lastMine) {
        this.lastMine = lastMine;
    }
}
