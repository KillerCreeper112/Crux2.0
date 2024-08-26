package killercreepr.crux.data.communication.impl;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.data.communication.CreateSound;
import org.jetbrains.annotations.Nullable;

public class SimpleCreateBlockSoundGroup implements CreateBlockSoundGroup {
    protected final @Nullable CreateSound breakSound;
    protected final @Nullable CreateSound stepSound;
    protected final @Nullable CreateSound placeSound;
    protected final @Nullable CreateSound hitSound;
    protected final @Nullable CreateSound fallSound;

    public SimpleCreateBlockSoundGroup(@Nullable CreateSound breakSound, @Nullable CreateSound stepSound, @Nullable CreateSound placeSound, @Nullable CreateSound hitSound, @Nullable CreateSound fallSound) {
        this.breakSound = breakSound;
        this.stepSound = stepSound;
        this.placeSound = placeSound;
        this.hitSound = hitSound;
        this.fallSound = fallSound;
    }

    @Override
    public @Nullable CreateSound getBreakSound() {
        return breakSound;
    }

    @Override
    public @Nullable CreateSound getStepSound() {
        return stepSound;
    }

    @Override
    public @Nullable CreateSound getPlaceSound() {
        return placeSound;
    }

    @Override
    public @Nullable CreateSound getHitSound() {
        return hitSound;
    }

    @Override
    public @Nullable CreateSound getFallSound() {
        return fallSound;
    }
}
