package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxedBoundingBox;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.location.LocationFinder;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StructureScatterer {
    protected final @NotNull Location center;
    protected final @NotNull Collection<Structure> structures;
    protected final @NotNull NumberProvider scatterRangeX;
    protected final @NotNull NumberProvider scatterRangeY;
    protected final @NotNull NumberProvider scatterRangeZ;
    protected final @NotNull NumberProvider maxScatterAttempts;
    protected final @NotNull LocationFinder locationFinder;

    protected final @NotNull Map<Location, BoundingBox> placed = new HashMap<>();

    public StructureScatterer(@NotNull Location center, @NotNull Collection<Structure> structures, @NotNull NumberProvider scatterRangeX, @NotNull NumberProvider scatterRangeY, @NotNull NumberProvider scatterRangeZ, @NotNull NumberProvider maxScatterAttempts, @NotNull LocationFinder locationFinder) {
        this.center = center;
        this.structures = structures;
        this.scatterRangeX = scatterRangeX;
        this.scatterRangeY = scatterRangeY;
        this.scatterRangeZ = scatterRangeZ;
        this.maxScatterAttempts = maxScatterAttempts;
        this.locationFinder = locationFinder;
    }

    protected @Nullable InputContext inputContext;

    public void addPlacedStructure(@NotNull Structure structure, @NotNull Location spawn, double rotation){
        BoundingBox box = CruxedBoundingBox.wrap(structure.boundingBox())
            .centerPoint(structure.originPos())
            .moveTo(spawn)
            .rotateY(
                rotation, spawn.x()+.5, spawn.y()+.5, spawn.z()+.5
            )
            .box();
        placed.put(spawn, box);
    }

    public void scatter(){
        for(Structure structure : structures){
            Location spawn = findRandomSpot(structure, maxScatterAttempts.sample(inputContext).intValue());
            if(spawn==null) continue;

            double rotation = CruxMath.RANDOM.nextInt(4) * 90;
            StructurePlaceEvent event = structure.place(
                spawn, rotation
            );
            if(event.isCancelled()) continue;
            rotation = event.getRotation();
            addPlacedStructure(structure, spawn, rotation);
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

    public @NotNull Location getRandomSpotUnchecked(){
        int xRange = scatterRangeX.sample(inputContext).intValue();
        int yRange = scatterRangeY.sample(inputContext).intValue();
        int zRange = scatterRangeZ.sample(inputContext).intValue();

        if(CruxMath.RANDOM.nextBoolean()) xRange *= -1;
        if(CruxMath.RANDOM.nextBoolean()) yRange *= -1;
        if(CruxMath.RANDOM.nextBoolean()) zRange *= -1;

        return center.clone().add(xRange, yRange, zRange);
    }

    public @Nullable Location findRandomSpot(@NotNull Structure forStructure, int attempts){
        while(attempts > 0){
            attempts--;
            Location potentialSpawn = getRandomSpotUnchecked();
            if(impedesOnAlreadyPlacedStructures(forStructure, potentialSpawn)) continue;
            potentialSpawn = locationFinder.find(potentialSpawn);
            if(potentialSpawn != null) return potentialSpawn;
        }
        return null;
    }

    public @NotNull Location getCenter() {
        return center;
    }

    public @NotNull Collection<Structure> getStructures() {
        return structures;
    }

    public @NotNull NumberProvider getScatterRangeX() {
        return scatterRangeX;
    }

    public @NotNull NumberProvider getScatterRangeZ() {
        return scatterRangeZ;
    }

    public @NotNull NumberProvider getScatterRangeY() {
        return scatterRangeY;
    }

    public @NotNull NumberProvider getMaxScatterAttempts() {
        return maxScatterAttempts;
    }

    public @NotNull Map<Location, BoundingBox> getPlaced() {
        return placed;
    }

    public @Nullable InputContext getInputContext() {
        return inputContext;
    }

    public void setInputContext(@Nullable InputContext inputContext) {
        this.inputContext = inputContext;
    }
}
