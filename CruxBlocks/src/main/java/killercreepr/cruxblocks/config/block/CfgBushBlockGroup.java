package killercreepr.cruxblocks.config.block;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.GenericDirectionalBlockGroup;
import killercreepr.cruxblocks.block.standard.BushBlock;
import killercreepr.cruxblocks.block.standard.group.BushBlockGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgBushBlockGroup extends BushBlockGroup {
    protected final float hardness;
    protected final @Nullable CreateBlockSoundGroup soundGroup;
    public CfgBushBlockGroup(@NotNull Key key, float hardness, @Nullable CreateBlockSoundGroup soundGroup, @NotNull BushBlock... blocks) {
        super(key, blocks);
        this.hardness = hardness;
        this.soundGroup = soundGroup;
    }

    @Override
    public float getHardness() {
        return hardness;
    }

    @Override
    public @Nullable CreateBlockSoundGroup getSoundGroup() {
        return soundGroup;
    }
}
