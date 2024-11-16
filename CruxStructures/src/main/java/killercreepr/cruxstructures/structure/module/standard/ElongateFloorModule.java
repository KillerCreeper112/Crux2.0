package killercreepr.cruxstructures.structure.module.standard;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import killercreepr.crux.Crux;
import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.impl.FAWEStructure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import killercreepr.cruxstructures.util.CruxStructureUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ElongateFloorModule implements StructureModule {
    protected final @Nullable CruxBlockWrapper blockType;
    public ElongateFloorModule(@Nullable CruxBlockWrapper blockType) {
        this.blockType = blockType;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        if(!(structure instanceof FAWEStructure fawe)) return;
        CruxPosition atCrux = CruxPosition.block(at);

        World world = at.getWorld();
        getPositionsAtLowestY(fawe.getBlocks(rotation)).forEach(blockPos ->{
            CruxPosition worldPos = CruxStructureUtil.fromStructureToWorldPos(
                structure, atCrux, blockPos
            );

            Block block = world.getBlockAt(worldPos.toLocation(world));
            if(!block.isSolid()) return;

            CruxBlockWrapper toSet = blockType == null ? CruxBlockWrapper.blockData(block.getBlockData()) : blockType;
            setBlocks(block.getRelative(BlockFace.DOWN), toSet);
        });
    }

    public void setBlocks(Block b, CruxBlockWrapper toSet){
        int minY = b.getWorld().getMinHeight();

        //todo better implementation
        BlockStateHolder<?> state;
        if(toSet instanceof CruxBlockWrapper.Vanilla d){
            state = BukkitAdapter.adapt(d.getMaterial().createBlockData());
        }else if(toSet instanceof CruxBlockWrapper.VanillaData d){
            state = BukkitAdapter.adapt(d.getData());
        }else {
            Block finalBlock = b;
            Crux.scheduler().runTask(() ->{
                Block block = finalBlock;
                while(block.isEmpty() || block.isReplaceable()){
                    toSet.setBlock(block, false);
                    block = block.getRelative(BlockFace.DOWN);

                    if(block.getY() < minY) break;
                }
            });
            return;
        }

        try(EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(b.getWorld()))){
            while(b.isEmpty() || b.isReplaceable()){
                session.setBlock(b.getX(), b.getY(), b.getZ(), state);
                //toSet.setBlock(b, false);
                b = b.getRelative(BlockFace.DOWN);

                if(b.getY() < minY) break;
            }
        }
    }

    public static Collection<CruxPosition> getPositionsAtLowestY(Collection<CruxPosition> positions) {
        if (positions == null || positions.isEmpty()) {
            return Set.of();  // Return an empty list if the input is null or empty
        }

        // Find the minimum Y value
        int minY = positions.stream()
            .mapToInt(CruxPosition::blockY)
            .min()
            .orElseThrow(() -> new IllegalArgumentException("Collection is empty"));

        // Filter positions with the minimum Y value
        return positions.stream()
            .filter(pos -> pos.y() == minY)
            .collect(Collectors.toSet());
    }
}
