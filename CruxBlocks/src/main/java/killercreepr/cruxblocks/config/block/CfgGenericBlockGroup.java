package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.GenericBlockGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CfgGenericBlockGroup extends GenericBlockGroup {
    protected final float hardness;

    public CfgGenericBlockGroup(@NotNull Key key, float hardness, @NotNull CruxBlock... blocks) {
        super(key, blocks);
        this.hardness = hardness;
    }

    @Override
    public float getHardness() {
        return hardness;
    }
}
