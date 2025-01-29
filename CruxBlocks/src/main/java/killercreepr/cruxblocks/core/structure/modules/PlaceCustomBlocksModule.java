package killercreepr.cruxblocks.core.structure.modules;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.core.component.PlacedCustomBlocksComponent;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import killercreepr.cruxstructures.core.registries.StructureRegistries;
import killercreepr.cruxstructures.core.util.CruxStructureUtil;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

public class PlaceCustomBlocksModule implements StructureModule {
    protected final Map<Key, Collection<BlockPos>> blocks;
    protected final boolean save;

    public PlaceCustomBlocksModule(Map<Key, Collection<BlockPos>> blocks, boolean save) {
        this.blocks = blocks;
        this.save = save;
    }

    @Override
    public void onStructureHook(@NotNull Structure structure) {
        structure.set(CruxBlockComponents.PLACE_CUSTOM_BLOCKS, this);
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

    @Override
    public void onCreated(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation, @NotNull StoredStructure stored) {
        if(!save) return;
        Structure structure = StructureRegistries.STRUCTURES.get(stored.getStructureKey());
        CruxPosition worldCenter = CruxStructureUtil.fromStructureToWorldPos(structure, center, structure.originPos());

        Collection<CruxPosition> placed = new HashSet<>();
        blocks.values().forEach((positions) ->{
            for(BlockPos pos : positions){
                CruxPosition place = worldCenter.add(pos).rotateAroundY(worldCenter, rotation);
                placed.add(place);
            }
        });
        stored.set(CruxBlockComponents.PLACED_CUSTOM_BLOCKS, new PlacedCustomBlocksComponent(placed));
    }
}
