package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.util.Pair;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.util.CruxBlockFace;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class WallsModule implements StructureModule {
    protected final @NotNull Map<BlockFace, Wall> walls;
    protected final @NotNull Map<BlockFace, LootTable<Key>> corners;
    protected final @NotNull NumberProvider defaultWallSpacing;
    protected final @NotNull Map<BlockFace, NumberProvider> wallSpacing;
    protected final @NotNull WallRotationType wallRotationType;
    protected final @NotNull NumberProvider wallMaxYCheck;

    public WallsModule(@NotNull Map<BlockFace, Wall> walls, @NotNull Map<BlockFace, LootTable<Key>> corners, @NotNull NumberProvider defaultWallSpacing, @NotNull Map<BlockFace, NumberProvider> wallSpacing, @NotNull WallRotationType wallRotationType, @NotNull NumberProvider wallMaxYCheck) {
        this.walls = walls;
        this.corners = corners;
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
        walls.forEach((face, wall) ->{

            wall.place(this, at, ctx, face, wallRotation);

            /*List<Key> keys = wallLoot.populateLoot(ctx);
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

            wallStructure.place(validSpawn.getLocation(), wallRotation);*/
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
        for(int y = 1; y <= max; y++){
            Block check = start.getRelative(0, y, 0);
            if(isValidBlock(check)) return check;
        }

        for(int y = -1; y >= -max; y--){
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

    public static class WallPart{
        protected final @NotNull LootTable<Key> structure;
        protected final @Nullable NumberProvider spacing;

        public WallPart(@NotNull LootTable<Key> structure, @Nullable NumberProvider spacing) {
            this.structure = structure;
            this.spacing = spacing;
        }

        public @NotNull LootTable<Key> getStructure() {
            return structure;
        }

        public @Nullable NumberProvider getSpacing() {
            return spacing;
        }

    }

    public static class Wall{
        protected final @NotNull List<WallPart> structures;
        protected final @NotNull NumberProvider wallSpacing;
        public Wall(@NotNull List<WallPart> structures, @NotNull NumberProvider wallSpacing) {
            this.structures = structures;
            this.wallSpacing = wallSpacing;
        }

        public void place(@NotNull WallsModule module, @NotNull Location at, @NotNull LootContext ctx,
                          @NotNull BlockFace face, double rotation){

            List<Pair<Structure, WallPart>> toPlace = new ArrayList<>();
            structures.forEach((wallLoot) ->{
                List<Key> keys = wallLoot.getStructure().populateLoot(ctx);
                if(keys.isEmpty()) return;
                Structure wallStructure = StructureRegistries.STRUCTURES.get(keys.getFirst());
                if(wallStructure== null){
                    Crux.log(Level.WARNING, "No crux Structure found for wall " + face + " (" + keys.getFirst() + ")");
                    return;
                }
                toPlace.add(Pair.of(wallStructure, wallLoot));
            });

            int spacing = wallSpacing.value().intValue();

            int centerIndex = structures.size() / 2;
            Structure centerStructure = toPlace.get(centerIndex).getFirst();

            CruxPosition spawn = CruxPosition.location(
                at.clone().add(
                    face.getModX() * spacing,
                    face.getModY() * spacing,
                    face.getModZ() * spacing
                )
            );

            spawn = spawn.rotateAroundY(CruxPosition.location(at), rotation);

            Block validSpawn = module.findValidBlock(spawn.getBlock(at.getWorld()));
            if(validSpawn != null){
                centerStructure.place(validSpawn.getLocation(), rotation);
            }

            double centerStructureSpacing = getWidth(centerStructure.boundingBox(), face);
            if(structures.size() % 2 == 0) centerStructureSpacing /= 2;

            double currentWidth = centerStructureSpacing;
            for(int i = centerIndex+1; i < structures.size(); i++){
                Structure wall = toPlace.get(i).getFirst();

                int index = i - (centerIndex+1)+1;
                spawnWall(toPlace.get(i), index, currentWidth, face, at, spacing, rotation, module, CruxBlockFace.rotateRight(face));

                currentWidth += getWidth(wall.boundingBox(), face);
            }

            currentWidth = centerStructureSpacing;
            for(int i = centerIndex-1; i > -1; i--){
                Structure wall = toPlace.get(i).getFirst();

                int index = ((centerIndex-1) - i)+1;
                spawnWall(toPlace.get(i), index, currentWidth, face, at, spacing, rotation, module, CruxBlockFace.rotateLeft(face));

                currentWidth += getWidth(wall.boundingBox(), face);
            }
        }

        public void spawnWall(Pair<Structure, WallPart> wall, int index, double currentWidth, BlockFace face,
                              Location at, int spacing, double rotation,
                              WallsModule module, BlockFace side){

            int addon = (int) Math.ceil(currentWidth + ((getWidth(wall.getFirst().boundingBox(), face) / 2) * index));

            WallPart part = wall.getSecond();
            if(part.getSpacing() != null) spacing = part.getSpacing().value().intValue();

            CruxPosition spawn = CruxPosition.location(
                at.clone().add(
                    face.getModX() * spacing + (side.getModX() * addon),
                    face.getModY() * spacing + (side.getModY() * addon),
                    face.getModZ() * spacing + (side.getModZ() * addon)
                )
            );

            spawn = spawn.rotateAroundY(CruxPosition.location(at), rotation);

            Block validSpawn = module.findValidBlock(spawn.getBlock(at.getWorld()));
            if(validSpawn != null){
                wall.getFirst().place(validSpawn.getLocation(), rotation);
            }
        }

        public static double getWidth(BoundingBox boundingBox, BlockFace direction) {
            return switch (direction) {
                case EAST, WEST -> boundingBox.getWidthX();
                //case NORTH, SOUTH -> boundingBox.getMaxZ() - boundingBox.getMinZ();
                default -> boundingBox.getWidthZ();
            };
        }

        public @NotNull List<WallPart> getStructures() {
            return structures;
        }

        public @NotNull NumberProvider getWallSpacing() {
            return wallSpacing;
        }
    }
}
