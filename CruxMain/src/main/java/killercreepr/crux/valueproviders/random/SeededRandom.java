package killercreepr.crux.valueproviders.random;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SeededRandom implements RandomHolder{
    protected final @NotNull Random random;
    protected long seed;

    public SeededRandom(@NotNull Random random, long seed) {
        this.random = random;
        this.seed = seed;
        random.setSeed(seed);
    }

    public SeededRandom(long seed) {
        this(new Random(), seed);
    }

    public SeededRandom setSeed(long seed){
        this.seed = seed;
        random.setSeed(seed);
        return this;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public @NotNull Random value() {
        return random;
    }
}
