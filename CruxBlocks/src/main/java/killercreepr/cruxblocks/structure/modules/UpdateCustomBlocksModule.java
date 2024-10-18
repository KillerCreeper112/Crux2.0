package killercreepr.cruxblocks.structure.modules;

import killercreepr.crux.Crux;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.CruxBlocksModule;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import killercreepr.cruxstructures.util.CruxStructureUtil;
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
