package killercreepr.cruxblocks;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxEntity;
import killercreepr.crux.util.CruxLoc;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxTickable;
import killercreepr.cruxblocks.listener.CustomBlocksListener;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CruxBlocksModule implements CruxModule {
    public static final String NAMESPACE = "CruxBlocks";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final Map<Block, ActiveCruxBlock> activeBlocks = new HashMap<>();
    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new CustomBlocksListener()
        );
    }

    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at){
        if(activeBlocks.containsKey(at)) return activeBlocks.get(at);


    }

    public @NotNull BukkitRunnable blockTick(){
        return new BukkitRunnable() {
            private int checkBlocks = 40;
            @Override
            public void run() {
                if(checkBlocks > 0){
                    checkBlocks--;
                    if(checkBlocks <= 0){
                        checkBlocks = 40;
                        for(Player p : Bukkit.getOnlinePlayers()){
                            if(!p.getChunk().isLoaded()) continue;
                            for(Block b : CruxLoc.getNearbyBlocks(p.getLocation().getBlock(), 24)){
                                if(!b.getChunk().isLoaded()) continue;
                                ActiveBlock active = CustomBlock.getActiveBlock(b);
                                if(active == null) continue;
                                if(active instanceof ActiveTickable && !containsTickedBlock(active)){
                                    addTickedBlock(active);
                                }
                            }
                        }
                    }
                }

                for(ActiveCruxBlock a : new HashSet<>(activeBlocks.values())){
                    if(!(a instanceof ActiveCruxTickable t)) continue;
                    if(a.isStop() || !a.getBlock().getChunk().isLoaded() ||
                        CruxEntity.getEntitiesNear(Player.class, a.getBlock().getLocation(), 32f, null).isEmpty()){
                        a.setStop(true);
                        t.stop();
                        activeBlocks.remove(a.getBlock());
                        continue;
                    }
                    if(!a.isActive()){
                        a.setStop(true);
                        return;
                    }
                    t.tick();
                }
            }
        };
    }
}
