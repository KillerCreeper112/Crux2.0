package killercreepr.cruxblocks.core.structure.modules;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import killercreepr.cruxstructures.core.util.CruxStructureUtil;
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
                CruxPosition place = center.add(pos).rotateAroundY(center, rotation);
                Block block = place.getBlock(world);
                group.placeBlock(PlaceBlockContext.context(block, null, BlockFace.DOWN));
            }
        });
    }
}
