package killercreepr.crux.api.block.sound;

import killercreepr.crux.api.communication.CreateSound;
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
