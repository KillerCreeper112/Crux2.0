package killercreepr.cruxblocks.block.texture;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextureData {
    //todo open this up
    static @Nullable TextureData buildFromBlock(@NotNull Block block){
        return buildFromBlockData(block.getBlockData());
    }

    static @Nullable TextureData buildFromBlockData(@NotNull BlockData data){
        if(data instanceof NoteBlock note){
            return new NoteTextureData(note.getNote(), note.getInstrument(), note.isPowered());
        }
        if(data instanceof Tripwire wire){
            return new WireTextureData.Builder()
                .attached(wire.isAttached())
                .disarmed(wire.isDisarmed())
                .faces(wire.getAllowedFaces())
                .powered(wire.isPowered())
                .build();
        }
        return new MaterialTextureData(data.getMaterial());
    }

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
    void setBlock(@NotNull LimitedRegion region, int x, int y, int z);
}
