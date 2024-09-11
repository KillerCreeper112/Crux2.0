package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxedBoundingBox;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StructureScatterer {
    protected final @NotNull Location center;
    protected final @NotNull Collection<Structure> structures;
    protected final @NotNull NumberProvider scatterRange;
    protected final @NotNull NumberProvider scatterRangeY;
    protected final @NotNull NumberProvider maxScatterAttempts;

    protected final @NotNull Map<Location, BoundingBox> placed = new HashMap<>();
    public StructureScatterer(@NotNull Location center, @NotNull Collection<Structure> structures, @NotNull NumberProvider scatterRange, @NotNull NumberProvider scatterRangeY, @NotNull NumberProvider maxScatterAttempts) {
        this.center = center;
        this.structures = structures;
        this.scatterRange = scatterRange;
        this.scatterRangeY = scatterRangeY;
        this.maxScatterAttempts = maxScatterAttempts;
    }

    public void scatter(){
        for(Structure structure : structures){
            Location spawn = findRandomSpot(structure, center.getWorld(), maxScatterAttempts.value().intValue());
            if(spawn==null) continue;

            double rotation = CruxMath.RANDOM.nextInt(4) * 90;
            StructurePlaceEvent event = structure.place(
                spawn, rotation
            );
            if(event.isCancelled()) continue;
            rotation = event.getRotation();
            BoundingBox box = CruxedBoundingBox.wrap(structure.boundingBox())
                .centerPoint(structure.originPos())
                .moveTo(spawn)
                .rotateY(
                    rotation, spawn.x()+.5, spawn.y()+.5, spawn.z()+.5
                )
                .box();
            placed.put(spawn, box);
        }
    }

    public boolean impedesOnAlreadyPlacedStructures(@NotNull Structure structure, @NotNull Location at){
        BoundingBox box = CruxedBoundingBox.wrap(structure.boundingBox())
            .centerPoint(structure.originPos())
            .moveTo(at)
            .box();
        for(BoundingBox placedBox : placed.values()){
            if(box.overlaps(placedBox)) return true;
        }
        return false;
    }

    public @NotNull Location getRandomSpotUnchecked(@NotNull World world){
        int xRange = scatterRange.value().intValue();
        int yRange = scatterRangeY.value().intValue();
        int zRange = scatterRange.value().intValue();

        return new Location(world, xRange, yRange, zRange);
    }

    public @Nullable Location findRandomSpot(@NotNull Structure forStructure, @NotNull World world, int attempts){
        while(attempts > 0){
            attempts--;
            Location potentialSpawn = getRandomSpotUnchecked(world);
            if(impedesOnAlreadyPlacedStructures(forStructure, potentialSpawn)) continue;
            return potentialSpawn;
        }
        return null;
    }
}
