package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.impl.FAWEStructure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import killercreepr.cruxstructures.util.CruxStructureUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ElongateFloorModule implements StructureModule {
    protected final @Nullable
    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        if(!(structure instanceof FAWEStructure fawe)) return;
        CruxPosition atCrux = CruxPosition.block(at);

        CruxPosition structurePos = CruxStructureUtil.fromWorldToStructurePos(
            structure, atCrux, atCrux
        );

        getPositionsAtLowestY(fawe.getBlocks(rotation)).forEach(block ->{

        });

        fawe.getBlocks(rotation).forEach(block ->{
            CruxPosition worldPos = CruxStructureUtil.fromStructureToWorldPos(
                structure, atCrux, block
            );
        });
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
