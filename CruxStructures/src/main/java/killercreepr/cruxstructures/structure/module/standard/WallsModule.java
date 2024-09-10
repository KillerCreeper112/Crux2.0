package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.module.StructureModule;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WallsModule implements StructureModule {
    protected final @NotNull Map<BlockFace, StructureGenerator> walls;
    protected final @NotNull NumberProvider defaultWallSpacing;
    protected final @NotNull Map<BlockFace, NumberProvider> wallSpacing;
    protected final @NotNull WallRotationType wallRotationType;
    public WallsModule(@NotNull Map<BlockFace, StructureGenerator> walls, @NotNull NumberProvider defaultWallSpacing, @NotNull Map<BlockFace, NumberProvider> wallSpacing, @NotNull WallRotationType wallRotationType) {
        this.walls = walls;
        this.defaultWallSpacing = defaultWallSpacing;
        this.wallSpacing = wallSpacing;
        this.wallRotationType = wallRotationType;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        double wallRotation = switch (wallRotationType){
            case RANDOM -> (CruxMath.RANDOM.nextInt( 4) * 90);
            case NONE -> 0D;
            default -> rotation;
        };
        walls.forEach((face, wallStructure) ->{
            int width = wallSpacing.getOrDefault(face, defaultWallSpacing).value().intValue();

            CruxPosition spawn = CruxPosition.location(
                at.clone().add(
                    face.getModX() * width,
                    face.getModY() * width,
                    face.getModZ() * width
                )
            );

            spawn = spawn.rotateAroundY(CruxPosition.location(at), wallRotation);

            structure.place(spawn.toLocation(at.getWorld()), wallRotation);
        });
    }

    public enum WallRotationType{
        RANDOM,
        STRUCTURE,
        NONE
    }
}
