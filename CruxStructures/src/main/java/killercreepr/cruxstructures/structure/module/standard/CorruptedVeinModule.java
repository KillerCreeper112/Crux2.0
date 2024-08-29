package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.util.CruxLoc;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CorruptedVeinModule implements StructureModule {
    protected final @NotNull NumberProvider veinLength;
    protected final @NotNull NumberProvider veinRotate;
    protected final @NotNull NumberProvider veinRotateY;
    protected final @NotNull Key veinBlock;

    public CorruptedVeinModule(@NotNull NumberProvider veinLength, @NotNull NumberProvider veinRotate, @NotNull NumberProvider veinRotateY, @NotNull Key veinBlock) {
        this.veinLength = veinLength;
        this.veinRotate = veinRotate;
        this.veinRotateY = veinRotateY;
        this.veinBlock = veinBlock;
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
            Crux.handlers().block().getBlock(veinBlock)
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
        if(block.isEmpty() || block.isReplaceable()){
            Block below = block.getRelative(BlockFace.DOWN);
            if(!below.isSolid()) return false;
            return true;
        }

        if(!block.isSolid()) return false;
        Block above = block.getRelative(BlockFace.UP);
        if(above.isReplaceable() || above.isEmpty()) return true;
        return false;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        Location current = at.clone();

        for(BlockFace face : BlockFace.values()){
            if(face == BlockFace.UP || face == BlockFace.DOWN) continue;
            current.setDirection(face.getDirection());
            generateVein(current, veinLength.sample().intValue(), veinRotate, veinRotateY);
        }
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
