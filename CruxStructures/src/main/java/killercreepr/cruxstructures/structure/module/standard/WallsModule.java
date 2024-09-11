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
import killercreepr.cruxstructures.location.LocationFinder;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
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
    protected final @NotNull LocationFinder locationFinder;
    protected final boolean defaultAutoRotate;

    public WallsModule(@NotNull Map<BlockFace, Wall> walls, @NotNull Map<BlockFace, LootTable<Key>> corners, @NotNull NumberProvider defaultWallSpacing, @NotNull Map<BlockFace, NumberProvider> wallSpacing, @NotNull WallRotationType wallRotationType, @NotNull LocationFinder locationFinder, boolean defaultAutoRotate) {
        this.walls = walls;
        this.corners = corners;
        this.defaultWallSpacing = defaultWallSpacing;
        this.wallSpacing = wallSpacing;
        this.wallRotationType = wallRotationType;
        this.locationFinder = locationFinder;
        this.defaultAutoRotate = defaultAutoRotate;
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
        });
    }

    public @NotNull Map<BlockFace, Wall> getWalls() {
        return walls;
    }

    public @NotNull Map<BlockFace, LootTable<Key>> getCorners() {
        return corners;
    }

    public @NotNull NumberProvider getDefaultWallSpacing() {
        return defaultWallSpacing;
    }

    public @NotNull Map<BlockFace, NumberProvider> getWallSpacing() {
        return wallSpacing;
    }

    public @NotNull WallRotationType getWallRotationType() {
        return wallRotationType;
    }

    public @NotNull LocationFinder getLocationFinder() {
        return locationFinder;
    }

    public boolean isDefaultAutoRotate() {
        return defaultAutoRotate;
    }

    public enum WallRotationType{
        RANDOM,
        STRUCTURE,
        NONE
    }

    public static class WallPart{
        protected final @NotNull LootTable<Key> structure;
        protected final @Nullable NumberProvider spacing;
        protected final @Nullable NumberProvider offset;
        protected final @Nullable Boolean autoRotate;

        public WallPart(@NotNull LootTable<Key> structure, @Nullable NumberProvider spacing, @Nullable NumberProvider offset, @Nullable Boolean autoRotate) {
            this.structure = structure;
            this.spacing = spacing;
            this.offset = offset;
            this.autoRotate = autoRotate;
        }

        public double rotate(double rotation, @NotNull BlockFace direction, boolean defaultRotate){
            boolean x = autoRotate == null ? defaultRotate : autoRotate;
            if(!x) return rotation;
            return CruxMath.wrap(rotation + getRotationAddon(direction), 0D, 360D);
        }

        public @Nullable Boolean getAutoRotate() {
            return autoRotate;
        }

        public @Nullable NumberProvider getOffset() {
            return offset;
        }

        public @NotNull LootTable<Key> getStructure() {
            return structure;
        }

        public @Nullable NumberProvider getSpacing() {
            return spacing;
        }

        public static double getRotationAddon(BlockFace direction){
            return switch (direction){
                case WEST -> 90;
                case NORTH -> -90;
                case EAST -> 180;
                default -> 0D;
            };
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

            int centerIndex = structures.size() / 2;
            Structure centerStructure = toPlace.get(centerIndex).getFirst();
            WallPart centerPart = toPlace.get(centerIndex).getSecond();

            int centerSpacing = centerPart.getSpacing() == null ? wallSpacing.value().intValue() : centerPart.getSpacing().value().intValue();

            BlockFace rightSide = CruxBlockFace.rotateRight(face);
            BlockFace leftSide = CruxBlockFace.rotateLeft(face);
            int addon = structures.size() % 2 == 0 ? (int) (getLength(centerStructure.boundingBox(), face) / 2D) : 0;

            CruxPosition spawn = CruxPosition.location(
                at.clone().add(
                    face.getModX() * centerSpacing + (rightSide.getModX() * addon),
                    face.getModY() * centerSpacing + (rightSide.getModY() * addon),
                    face.getModZ() * centerSpacing + (rightSide.getModZ() * addon)
                )
            );

            spawn = spawn.rotateAroundY(CruxPosition.location(at), rotation);

            Location validSpawn = module.locationFinder.find(spawn.toLocation(at.getWorld()));
            if(validSpawn != null){
                centerStructure.place(validSpawn, centerPart.rotate(rotation, face, module.defaultAutoRotate));
            }

            double centerStructureSpacing = getLength(centerStructure.boundingBox(), face);
            int doubleRightAddon;
            if(structures.size() % 2 == 0){
                doubleRightAddon = (int) Math.ceil(centerStructureSpacing-1);
                centerStructureSpacing /= 2;
            }else doubleRightAddon = 0;

            double currentWidth = centerStructureSpacing;
            for(int i = centerIndex+1; i < structures.size(); i++){
                Structure wall = toPlace.get(i).getFirst();

                spawnWall(toPlace.get(i), currentWidth + doubleRightAddon, face, at, wallSpacing.value().intValue(),
                    rotation, module, rightSide);

                currentWidth += getLength(wall.boundingBox(), face);
            }

            currentWidth = centerStructureSpacing;
            for(int i = centerIndex-1; i > -1; i--){
                Structure wall = toPlace.get(i).getFirst();

                spawnWall(toPlace.get(i), currentWidth, face, at, wallSpacing.value().intValue(),
                    rotation, module, leftSide);

                currentWidth += getLength(wall.boundingBox(), face);
            }
        }

        public void spawnWall(Pair<Structure, WallPart> wall, double currentWidth, BlockFace face,
                              Location at, int spacing, double rotation,
                              WallsModule module, BlockFace side){

            int addon = (int) Math.ceil(currentWidth /*+ ((getLength(wall.getFirst().boundingBox(), face) / 2) * index)*/);

            WallPart part = wall.getSecond();
            if(part.getSpacing() != null) spacing = part.getSpacing().value().intValue();

            int offset = part.getOffset() == null ? 0 : part.getOffset().value().intValue();

            CruxPosition spawn = CruxPosition.location(
                at.clone().add(
                    face.getModX() * spacing + (side.getModX() * addon) + (side.getModX() * offset),
                    face.getModY() * spacing + (side.getModY() * addon) + (side.getModY() * offset),
                    face.getModZ() * spacing + (side.getModZ() * addon) + (side.getModZ() * offset)
                )
            );

            spawn = spawn.rotateAroundY(CruxPosition.location(at), rotation);

            Location validSpawn = module.locationFinder.find(spawn.toLocation(at.getWorld()));
            if(validSpawn != null){
                wall.getFirst().place(validSpawn, part.rotate(rotation, face, module.defaultAutoRotate));
            }
        }

        public static double getWidth(BoundingBox boundingBox, BlockFace direction) {
            return switch (direction) {
                case EAST, WEST -> boundingBox.getWidthX();
                //case NORTH, SOUTH -> boundingBox.getMaxZ() - boundingBox.getMinZ();
                default -> boundingBox.getWidthZ();
            };
        }

        public static double getLength(BoundingBox boundingBox, BlockFace direction) {
            return switch (direction) {
                case EAST, WEST -> boundingBox.getWidthZ()+1;
                //case NORTH, SOUTH -> boundingBox.getMaxZ() - boundingBox.getMinZ();
                default -> boundingBox.getWidthX()+1;
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
