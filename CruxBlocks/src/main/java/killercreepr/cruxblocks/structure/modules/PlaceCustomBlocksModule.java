package killercreepr.cruxblocks.structure.modules;

import killercreepr.crux.Crux;
import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.registries.CruxBlocksRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import killercreepr.cruxstructures.util.CruxStructureUtil;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

public class PlaceCustomBlocksModule implements StructureModule {
    protected final Map<Key, Collection<BlockPos>> blocks;

    public PlaceCustomBlocksModule(Map<Key, Collection<BlockPos>> blocks) {
        this.blocks = blocks;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        World world = at.getWorld();
        CruxPosition placedAt = CruxPosition.location(at);
        CruxPosition center = CruxStructureUtil.fromStructureToWorldPos(structure, placedAt, structure.originPos());
        blocks.forEach((key, positions) ->{
            CruxBlockGroup group = CruxBlocksRegistries.BLOCK.getGroup(key);
            if(group == null){
                Crux.log(Level.WARNING, "CruxBlockGroup " + key + " not found! PlaceCustomPlacesModule");
                return;
            }
            for(BlockPos pos : positions){
                CruxPosition place = center.add(pos);
                Block block = place.getBlock(world);
                group.placeBlock(PlaceBlockContext.context(block, null, BlockFace.DOWN));
            }
        });
    }
}
