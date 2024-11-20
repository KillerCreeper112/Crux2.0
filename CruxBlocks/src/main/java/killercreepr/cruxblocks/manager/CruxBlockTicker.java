package killercreepr.cruxblocks.manager;

import killercreepr.crux.api.game.Statutable;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.registries.CruxBlocksRegistries;
import killercreepr.cruxblocks.registry.CruxBlockRegistry;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxBlockTicker extends Statutable {
    static CruxBlockTicker simple(Plugin plugin){
        return simple(plugin, CruxBlocksRegistries.BLOCK);
    }
    static CruxBlockTicker simple(Plugin plugin, CruxBlockRegistry registry){
        return new SimpleCruxBlockTicker(plugin, registry);
    }
    @Nullable
    Collection<ActiveCruxBlock> getActiveBlocks(@NotNull Chunk chunk);
    @Nullable
    ActiveCruxBlock getActiveBlock(@NotNull Block at);
    @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data);
    boolean hasTickedBlock(@NotNull Block at);

    @Nullable ActiveCruxBlock getTickedBlock(@NotNull Block at);
}
