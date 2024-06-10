package killercreepr.cruxblocks.block;

import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.group.CruxDirectionalBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import net.minecraft.world.level.block.DirectionalBlock;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class GenericDirectionalBlock extends GenericBlock implements CruxBlockDirectional{
    protected final @NotNull BlockFace direction;
    public GenericDirectionalBlock(@NotNull Key key, @NotNull TextureData textureData, @NotNull BlockFace direction) {
        super(key, textureData);
        this.direction = direction;
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        Set<BlockFace> faces = new HashSet<>();
        for(CruxBlock block : getGroup()){
            CruxBlockDirectional directional = (CruxBlockDirectional) block;
            faces.add(directional.getDirection());
        }
        return faces;
    }

    @Override
    public BlockFace getFace(float pitch, BlockFace face) {
        return null;
    }

    @Override
    public @NotNull BlockFace getDirection() {
        return direction;
    }
}
