package killercreepr.cruxblocks.block.texture;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextureData {
    boolean compareTexture(@Nullable Block b);
    boolean compareTexture(@Nullable BlockData data);
    default boolean compareTexture(@Nullable BlockState data){
        if(data==null) return false;
        return compareTexture(data.getBlockData());
    }
    boolean compareTexture(@Nullable TextureData data);

    @NotNull BlockData applyToBlockData(@NotNull BlockData data);
    default @NotNull BlockData applyToBlock(@NotNull Block block, boolean applyPhysics){
        BlockData data = applyToBlockData(block.getBlockData());
        block.setBlockData(data, applyPhysics);
        return data;
    }

    void setBlock(@NotNull Block block, boolean applyPhysics);
}
