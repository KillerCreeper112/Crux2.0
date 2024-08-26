package killercreepr.cruxblocks.config.block;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.GenericBlockGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgGenericBlockGroup extends GenericBlockGroup {
    protected final float hardness;
    protected final @Nullable CreateBlockSoundGroup soundGroup;

    public CfgGenericBlockGroup(@NotNull Key key, float hardness, @Nullable CreateBlockSoundGroup soundGroup, @NotNull CruxBlock... blocks) {
        super(key, blocks);
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
