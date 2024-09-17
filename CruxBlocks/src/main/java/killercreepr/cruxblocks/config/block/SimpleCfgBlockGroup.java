package killercreepr.cruxblocks.config.block;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.SimpleBlockGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleCfgBlockGroup extends SimpleBlockGroup implements CfgBlockGroup {
    final float hardness;
    final @Nullable CreateBlockSoundGroup soundGroup;
    final boolean requireCorrectToolForHarvest;

    public SimpleCfgBlockGroup(@NotNull Key key,
                               float hardness,
                               @Nullable CreateBlockSoundGroup soundGroup,
                               boolean requireCorrectToolForHarvest,
                               @NotNull CruxBlock... blocks) {
        super(key, blocks);
        this.hardness = hardness;
        this.soundGroup = soundGroup;
        this.requireCorrectToolForHarvest = requireCorrectToolForHarvest;
    }

    @Override
    public boolean isRequireCorrectToolForHarvest() {
        return requireCorrectToolForHarvest;
    }

    @Override
    public float getHardness() {
        return hardness;
    }
}
