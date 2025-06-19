package killercreepr.cruxblocks.api.block.component;

import killercreepr.crux.core.util.CruxDirection;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.core.mining.user.EntityMiner;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface DirectionalGroup extends CruxBlockGroupComponent, Iterable<CruxBlock> {
    @Nullable
    CruxBlock getBlock(@NotNull BlockFace direction);
    boolean isOrientable();

    default @Nullable CruxBlock getBlock(@NotNull Axis direction){
        return getBlock(CruxDirection.getFaceFromAxis(direction));
    }

    default @NotNull Set<BlockFace> getFaces() {
        Set<BlockFace> faces = new HashSet<>();
        for(CruxBlock block : this){
            DirectionalBlock directional = block.getComponents().get(CruxBlockComponents.DIRECTIONAL_BLOCK);
            if(directional==null) continue;
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
    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics, @NotNull CruxBlockGroup group) {
        CruxBlock base = group.getBaseBlock();
        if(!(ctx.getMiner() instanceof EntityMiner m) || !(m.getEntity() instanceof LivingEntity p)){
            BlockFace blockFace = ctx.getBlockFace();
            CruxBlock toPlace = getBlock(blockFace);
            if(toPlace==null) return base.placeBlock(ctx);
            return toPlace.placeBlock(ctx);
        }
        BlockFace blockFace = ctx.getBlockFace();
        float pitch = p.getLocation().getPitch();
        BlockFace face = isOrientable() ? blockFace.getOppositeFace() : p.getFacing().getOppositeFace();
        face = getFace(pitch, face);

        CruxBlock toPlace = getBlock(face);
        if(toPlace==null) return null;
        return toPlace.placeBlock(ctx);
    }
    class Simple implements DirectionalGroup{
        protected final @NotNull Map<BlockFace, CruxBlock> directionToBlock = new HashMap<>();
        protected final boolean orientable;

        public Simple(boolean orientable) {
            this.orientable = orientable;
        }

        @Override
        public void onRegistered(@NotNull CruxBlock block, @NotNull CruxBlockGroup group) {
            DirectionalBlock directional = block.getComponents().get(CruxBlockComponents.DIRECTIONAL_BLOCK);
            if(directional==null) return;
            directionToBlock.put(directional.getDirection(), block);
        }

        @Override
        public @Nullable CruxBlock getBlock(@NotNull BlockFace direction) {
            return directionToBlock.get(direction);
        }

        @Override
        public boolean isOrientable() {
            return orientable;
        }

        @NotNull
        @Override
        public Iterator<CruxBlock> iterator() {
            return directionToBlock.values().iterator();
        }
    }
}
