package killercreepr.crux.data.communication;

import org.jetbrains.annotations.Nullable;

public interface CreateBlockSoundGroup {
    @Nullable
    CreateSound getBreakSound();
    @Nullable
    CreateSound getStepSound();
    @Nullable
    CreateSound getPlaceSound();
    @Nullable
    CreateSound getHitSound();
    @Nullable
    CreateSound getFallSound();
}
