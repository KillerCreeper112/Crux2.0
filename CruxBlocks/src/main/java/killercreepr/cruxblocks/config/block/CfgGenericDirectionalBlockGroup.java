package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.GenericDirectionalBlockGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CfgGenericDirectionalBlockGroup extends GenericDirectionalBlockGroup {
    protected final float hardness;
    public CfgGenericDirectionalBlockGroup(@NotNull Key key, boolean orientable, float hardness, @NotNull CruxBlock... blocks) {
        super(key, orientable, blocks);
        this.hardness = hardness;
    }

    @Override
    public float getHardness() {
        return hardness;
    }


}
