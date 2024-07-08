package killercreepr.cruxblocks.block.group;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.CruxBlockDirectional;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.user.EntityMiner;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public interface CruxDirectionalBlockGroup extends CruxBlockGroup{
    static @NotNull BlockFace getFaceFromAxis(@NotNull Axis direction){
        return switch (direction){
            case X -> BlockFace.EAST;
            case Y -> BlockFace.UP;
            case Z -> BlockFace.NORTH;
        };
    }

    default@Nullable CruxBlockDirectional getBlock(@NotNull Axis direction){
        return getBlock(getFaceFromAxis(direction));
    }

    default @NotNull Set<BlockFace> getFaces() {
        Set<BlockFace> faces = new HashSet<>();
        for(CruxBlock block : this){
            CruxBlockDirectional directional = (CruxBlockDirectional) block;
            faces.add(directional.getDirection());
        }
        return faces;
    }

    default @NotNull BlockFace getFace(float pitch, @NotNull BlockFace face){
        Set<BlockFace> faces = getFaces();
        if(isOrientable()){
            return switch (face){
                case DOWN, UP -> BlockFace.UP;
                case WEST, EAST -> BlockFace.EAST;
                default -> BlockFace.NORTH;
            };
        }
        if(faces.contains(BlockFace.UP) && pitch <= -45) face = BlockFace.DOWN;
        if(faces.contains(BlockFace.DOWN) && pitch >= 45) face = BlockFace.UP;
        return faces.contains(face) ? face : face.getOppositeFace();
    }

    @Override
    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx) {
        CruxBlock base = getBaseBlock();
        if(!(ctx.getMiner() instanceof EntityMiner m) || !(m.getEntity() instanceof LivingEntity p)) return base.placeBlock(ctx);
        BlockFace blockFace = ctx.getBlockFace();
        float pitch = p.getLocation().getPitch();
        BlockFace face = isOrientable() ? blockFace.getOppositeFace() : p.getFacing().getOppositeFace();
        face = getFace(pitch, face);

        CruxBlockDirectional toPlace = getBlock(face);
        if(toPlace==null) return null;
        return toPlace.placeBlock(ctx);
    }

    boolean isOrientable();
    @Nullable CruxBlockDirectional getBlock(@NotNull BlockFace direction);
}
