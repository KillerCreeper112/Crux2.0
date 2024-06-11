package killercreepr.cruxblocks;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.command.CruxBlocksCommands;
import killercreepr.cruxblocks.data.entity.MinerHolder;
import killercreepr.cruxblocks.item.CruxItemsItemProvider;
import killercreepr.cruxblocks.item.KeyedItemProvider;
import killercreepr.cruxblocks.listener.CustomBlocksListener;
import killercreepr.cruxblocks.manager.CruxBlockManager;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CruxBlocksModule implements CruxModule, CruxBlockManager {
    public static final String NAMESPACE = "CruxBlocks";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final Map<Block, ActiveCruxBlock> activeBlocks = new HashMap<>();
    protected @Nullable KeyedItemProvider keyedItemProvider;

    public Map<Block, ActiveCruxBlock> getActiveBlocks() {
        return activeBlocks;
    }

    public @Nullable KeyedItemProvider getKeyedItemProvider() {
        return keyedItemProvider;
    }

    public void setKeyedItemProvider(@Nullable KeyedItemProvider keyedItemProvider) {
        this.keyedItemProvider = keyedItemProvider;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new CustomBlocksListener(this)
        );

        EntityMemory.registerFunction(plugin, e ->{
            if(!(e instanceof PlayerMemory p)) return;
            p.getHolders().register(new MinerHolder(p, this));
        });

        CruxBlocksCommands.register(plugin);

        if(CruxRegistries.MODULES.containsKey("CruxItems")){
            keyedItemProvider = new CruxItemsItemProvider();
        }else keyedItemProvider = null;
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at){
        return getActiveBlock(at, at.getBlockData());
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data) {
        if(activeBlocks.containsKey(at)) return activeBlocks.get(at);
        CruxBlock block = CruxBlocksRegistries.BLOCKS.getByBlockData(data);
        return block==null?null:block.createActive(at);
    }

    public @NotNull BukkitRunnable blockTick(){
        //todo
        return new BukkitRunnable() {
            @Override
            public void run() {
            }
        };
    }
}
