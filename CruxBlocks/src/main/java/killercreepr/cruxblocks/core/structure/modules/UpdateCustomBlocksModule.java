package killercreepr.cruxblocks.core.structure.modules;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxblocks.core.CruxBlocksModule;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import killercreepr.cruxstructures.core.util.CruxStructureUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class UpdateCustomBlocksModule implements StructureModule {
    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        World world = at.getWorld();
        Crux.getServer().getScheduler().runTaskAsynchronously(Crux.getMainPlugin(), task ->{
            CruxBlocksModule cruxBlocks = CruxRegistries.MODULES.getModuleOrThrow(CruxBlocksModule.class);
            structure.getBlocks(rotation).forEach(pos ->{
                pos = CruxStructureUtil.fromStructureToWorldPos(structure, CruxPosition.location(at), pos);
                Block block = pos.getBlock(world);
                cruxBlocks.getActiveBlock(block);
            });
        });
    }
}
