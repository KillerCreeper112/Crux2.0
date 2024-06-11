package killercreepr.cruxblocks.block.group;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.CruxBlockDirectional;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericDirectionalBlockGroup extends GenericBlockGroup implements CruxDirectionalBlockGroup{
    protected final Map<BlockFace, CruxBlockDirectional> blockFaceToBlock = new HashMap<>();
    protected final boolean orientable;
    public GenericDirectionalBlockGroup(@NotNull Key key, boolean orientable, @NotNull CruxBlock... blocks) {
        super(key, blocks);
        this.orientable = orientable;
        for(CruxBlock b : group){
            if(!(b instanceof CruxBlockDirectional dir)) continue;
            blockFaceToBlock.put(dir.getDirection(), dir);
        }
    }

    @Override
    public @Nullable CruxBlockDirectional getBlock(@NotNull BlockFace direction) {
        return blockFaceToBlock.get(direction);
    }

    @Override
    public boolean isOrientable() {
        return orientable;
    }
}
