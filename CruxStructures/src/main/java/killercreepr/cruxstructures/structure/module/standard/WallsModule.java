package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class WallsModule implements StructureModule {
    protected final @NotNull Map<BlockFace, LootTable<Key>> walls;
    protected final @NotNull NumberProvider defaultWallSpacing;
    protected final @NotNull Map<BlockFace, NumberProvider> wallSpacing;
    protected final @NotNull WallRotationType wallRotationType;
    protected final @NotNull NumberProvider wallMaxYCheck;

    public WallsModule(@NotNull Map<BlockFace, LootTable<Key>> walls, @NotNull NumberProvider defaultWallSpacing, @NotNull Map<BlockFace, NumberProvider> wallSpacing, @NotNull WallRotationType wallRotationType, @NotNull NumberProvider wallMaxYCheck) {
        this.walls = walls;
        this.defaultWallSpacing = defaultWallSpacing;
        this.wallSpacing = wallSpacing;
        this.wallRotationType = wallRotationType;
        this.wallMaxYCheck = wallMaxYCheck;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        double wallRotation = switch (wallRotationType){
            case RANDOM -> (CruxMath.RANDOM.nextInt( 4) * 90);
            case NONE -> 0D;
            default -> rotation;
        };
        LootContext ctx = LootContext.builder()
            .location(at)
            .info(DataExchange.builder()
                .put("structure", structure)
                .put("rotation", rotation)
                .put("structure_module", this)
                .build())
            .build();
        walls.forEach((face, wallLoot) ->{
            List<Key> keys = wallLoot.populateLoot(ctx);
            if(keys.isEmpty()) return;
            Structure wallStructure = StructureRegistries.STRUCTURES.get(keys.getFirst());
            if(wallStructure== null){
                Crux.log(Level.WARNING, "No crux Structure found for wall " + face + " (" + keys.getFirst() + ")");
                return;
            }

            int width = wallSpacing.getOrDefault(face, defaultWallSpacing).value().intValue();

            CruxPosition spawn = CruxPosition.location(
                at.clone().add(
                    face.getModX() * width,
                    face.getModY() * width,
                    face.getModZ() * width
                )
            );

            spawn = spawn.rotateAroundY(CruxPosition.location(at), wallRotation);

            Block validSpawn = findValidBlock(spawn.getBlock(at.getWorld()));
            if(validSpawn == null) return;

            wallStructure.place(validSpawn.getLocation(), wallRotation);
        });
    }

    public boolean isValidBlock(Block b){
        if(!b.isSolid()) return false;
        Block check = b.getRelative(BlockFace.UP);
        return check.isEmpty() || check.isPassable() || check.isReplaceable();
    }

    public Block findValidBlock(Block start){
        if(isValidBlock(start)) return start;
        int max = wallMaxYCheck.value().intValue();
        for(int y = -max; y <= max; y++){
            if(y==0) continue;
            Block check = start.getRelative(0, y, 0);
            if(isValidBlock(check)) return check;
        }
        return null;
    }

    public enum WallRotationType{
        RANDOM,
        STRUCTURE,
        NONE
    }
}
