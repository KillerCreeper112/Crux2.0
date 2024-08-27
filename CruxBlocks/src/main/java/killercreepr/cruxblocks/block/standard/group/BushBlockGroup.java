package killercreepr.cruxblocks.block.standard.group;

import killercreepr.cruxblocks.block.group.GenericBlockGroup;
import killercreepr.cruxblocks.block.standard.BushBlock;
import killercreepr.cruxblocks.block.standard.BushType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class BushBlockGroup extends GenericBlockGroup {
    protected final Map<BushType, BushBlock> bushTypeToBlock = new HashMap<>();
    public BushBlockGroup(@NotNull Key key, @NotNull BushBlock... blocks) {
        super(key, blocks);
        for(BushBlock b : blocks){
            bushTypeToBlock.put(b.getBushType(), b);
        }
    }

    public @Nullable BushBlock getBlock(@NotNull BushType type){
        return bushTypeToBlock.get(type);
    }

    public Map<BushType, BushBlock> getBushTypeToBlock() {
        return bushTypeToBlock;
    }
}
