package killercreepr.cruxblocks.config.block;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.GenericDirectionalBlockGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgDirectionalBlockGroup extends GenericDirectionalBlockGroup {
    protected final float hardness;
    protected final @Nullable CreateBlockSoundGroup soundGroup;
    public CfgDirectionalBlockGroup(@NotNull Key key, boolean orientable, float hardness, @Nullable CreateBlockSoundGroup soundGroup, @NotNull CruxBlock... blocks) {
        super(key, orientable, blocks);
        this.hardness = hardness;
        this.soundGroup = soundGroup;
    }

    @Override
    public float getHardness() {
        return hardness;
    }

    @Override
    public CreateBlockSoundGroup getSoundGroup() {
        return soundGroup;
    }
}
