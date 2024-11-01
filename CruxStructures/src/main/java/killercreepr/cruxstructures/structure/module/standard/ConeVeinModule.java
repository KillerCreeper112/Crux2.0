package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.block.predicate.BlockPredicate;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.util.CruxLoc;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.crux.valueproviders.vector.NumberVector;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import killercreepr.cruxstructures.util.CruxStructureUtil;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ConeVeinModule implements StructureModule {
    protected final @NotNull NumberProvider veinLength;
    protected final @NotNull NumberProvider veinRotate;
    protected final @NotNull NumberProvider veinRotateY;
    protected final @NotNull Key veinBlock;
    //double radius, int numPoints, double downwardPitch
    protected final @NotNull NumberProvider radius;
    protected final @NotNull NumberProvider numPoints;
    protected final @NotNull NumberProvider downwardPitch;
    protected final @Nullable NumberVector offset;
    protected final @Nullable BlockPredicate replaceableBlock;

    public ConeVeinModule(@NotNull NumberProvider veinLength, @NotNull NumberProvider veinRotate, @NotNull NumberProvider veinRotateY, @NotNull Key veinBlock, @NotNull NumberProvider radius, @NotNull NumberProvider numPoints, @NotNull NumberProvider downwardPitch, @Nullable NumberVector offset, @Nullable BlockPredicate replaceableBlock) {
        this.veinLength = veinLength;
        this.veinRotate = veinRotate;
        this.veinRotateY = veinRotateY;
        this.veinBlock = veinBlock;
        this.radius = radius;
        this.numPoints = numPoints;
        this.downwardPitch = downwardPitch;
        this.offset = offset;
        this.replaceableBlock = replaceableBlock;
    }

    public void generateVein(@NotNull Location start, int length, NumberProvider rotate, NumberProvider yRotate){
        int maxTries = 4;
        int tries = 0;
        CruxBlockWrapper veinBlock = getVeinBlock();
        while(length > 0){
            length--;
            start.setYaw(start.getYaw() + rotate.getMinValue().floatValue());
            start.setPitch(start.getPitch() + yRotate.getMinValue().floatValue());

            Block block = getBlockAboveOrBelow(start.getBlock());
            if(block==null){
                if(tries >= maxTries) break;
                tries++;
                start = CruxLoc.shift(start, 1D, 0D, 0D);
                continue;
            }
            veinBlock.setBlock(block, false);
            start = CruxLoc.shift(start, 1D, 0D, 0D);
        }
    }

    public CruxBlockWrapper getVeinBlock(){
        return Objects.requireNonNull(
            Crux.handlers().block().getBlockWrapper(veinBlock)
        );
    }

    public Block getBlockAboveOrBelow(@NotNull Block block){
        if(isPreferredBlock(block)) return block;
        Block b = block.getRelative(BlockFace.UP);
        if(isPreferredBlock(b)) return b;

        b = block.getRelative(BlockFace.DOWN);
        if(isPreferredBlock(b)) return b;
        return null;
    }

    public boolean isPreferredBlock(@NotNull Block block){
        if(replaceableBlock == null) return true;
        return replaceableBlock.test(Crux.handlers().block().getBlock(block));
        /*if(true) return true;//todo
        if(block.isEmpty() || block.isReplaceable()){
            Block below = block.getRelative(BlockFace.DOWN);
            if(!below.isSolid()) return false;
            return true;
        }

        if(!block.isSolid()) return false;
        Block above = block.getRelative(BlockFace.UP);
        if(above.isReplaceable() || above.isEmpty()) return true;
        return false;*/
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        Location current = at.clone();

        if(offset != null){
            CruxPosition pos = CruxPosition.location(current).add(
                offset.x().value().doubleValue(),
                offset.y().value().doubleValue(),
                offset.z().value().doubleValue()
            );
            current = pos.rotateAroundY(CruxPosition.location(at), rotation).toLocation(current.getWorld());
        }

        CruxLoc.getInterestingCone(
            current, radius.value().doubleValue(),
            numPoints.value().intValue(),
            downwardPitch.value().intValue()
        ).forEach(l ->{
            generateVein(l, veinLength.sample().intValue(), veinRotate, veinRotateY);
        });
    }

    public @NotNull NumberProvider getVeinLength() {
        return veinLength;
    }

    public @NotNull NumberProvider getVeinRotate() {
        return veinRotate;
    }

    public @NotNull NumberProvider getVeinRotateY() {
        return veinRotateY;
    }
}
